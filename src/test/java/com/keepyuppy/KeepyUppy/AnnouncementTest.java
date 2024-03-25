package com.keepyuppy.KeepyUppy;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.post.service.AnnouncementService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AnnouncementTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamService teamService;

    @Autowired
    AnnouncementService announcementService;

    private Users[] users;
    private CustomUserDetails[] userDetails;
    private PostRequest request;

    @BeforeEach
    public void setup(){
        users = new Users[3];
        userDetails = new CustomUserDetails[3];
        for (int i = 0; i < 3; i++) {
            Users user = Users.builder().name("name" + i)
                    .imageUrl(null)
                    .username("username" + i)
                    .oauthId(null)
                    .provider(Provider.GOOGLE)
                    .bio("bio")
                    .build();
            userRepository.save(user);
            users[i] = user;
            userDetails[i] = new CustomUserDetails(user);
        }

        CreateTeamRequest createTeamRequest = new CreateTeamRequest("team", "test", "red", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), List.of("username1"), null, null, null);
        teamService.createTeam(userDetails[0], createTeamRequest);

        request = new PostRequest("title", "content", true);
    }

    @Test
    @DisplayName("공지글 생성")
    void createAnnouncement() {
        //when
        AnnouncementResponse response = announcementService.createAnnouncement(userDetails[0], 1L, request);

        //then
        Assertions.assertEquals(request.getTitle(), response.getTitle());
        Assertions.assertEquals(request.getContent(), response.getContent());
        Assertions.assertEquals(false, response.isPinned());
    }

    @Test
    @DisplayName("공지글 조회")
    void viewAnnouncement() {
        announcementService.createAnnouncement(userDetails[0], 1L, request);

        //when
        AnnouncementResponse response1 = announcementService.viewAnnouncement(userDetails[0], 1L, 1L);
        AnnouncementResponse response2 = announcementService.viewAnnouncement(userDetails[1], 1L, 1L);

        //then
        Assertions.assertEquals(request.getTitle(), response1.getTitle());
        Assertions.assertEquals(request.getContent(), response1.getContent());
        Assertions.assertEquals(request.getTitle(), response2.getTitle());
        Assertions.assertEquals(request.getContent(), response2.getContent());
    }

    @Test
    @DisplayName("팀에 속하지 않은 유저는 실패")
    void notMember() {
        announcementService.createAnnouncement(userDetails[0], 1L, request);

        //then
        Assertions.assertThrows(CustomException.class, () -> announcementService.createAnnouncement(userDetails[2], 1L, request));
        Assertions.assertThrows(CustomException.class, () -> announcementService.viewAnnouncement(userDetails[2], 1L, 1L));
    }

    @Test
    @DisplayName("공지글 삭제")
    void deleteAnnouncement() {
        //given
        announcementService.createAnnouncement(userDetails[1], 1L, request);
        announcementService.createAnnouncement(userDetails[1], 1L, request);

        //when
        announcementService.deleteAnnouncement(userDetails[1], 1L, 1L);
        announcementService.deleteAnnouncement(userDetails[0], 1L, 2L);

        //then
        Assertions.assertThrows(CustomException.class, () -> announcementService.viewAnnouncement(userDetails[0], 1L, 1L));
        Assertions.assertThrows(CustomException.class, () -> announcementService.viewAnnouncement(userDetails[0], 1L, 2L));
    }

    @Test
    @DisplayName("공지글 삭제 권한이 없는 경우")
    void deleteAnnouncementFailure() {
        //given
        announcementService.createAnnouncement(userDetails[0], 1L, request);

        //then
        Assertions.assertThrows(CustomException.class, () -> announcementService.deleteAnnouncement(userDetails[1], 1L, 1L));
    }

    @Test
    @DisplayName("본인 공지글 수정")
    void updateAnnouncementByAuthor() {
        //given
        PostRequest editRequest = new PostRequest("title edit", "content edit", false);
        announcementService.createAnnouncement(userDetails[1], 1L, request);

        //when
        AnnouncementResponse response = announcementService.updateAnnouncement(userDetails[1], 1L, 1L, editRequest);

        //then
        Assertions.assertEquals(editRequest.getTitle(), response.getTitle());
        Assertions.assertEquals(editRequest.getContent(), response.getContent());
    }

    @Test
    @DisplayName("공지글 수정 권한이 없는 경우")
    void updateAnnouncementFailure() {
        //given
        announcementService.createAnnouncement(userDetails[1], 1L, request);

        //then
        Assertions.assertThrows(CustomException.class, () -> announcementService.updateAnnouncement(userDetails[0], 1L, 1L, request));
    }

    @Test
    @DisplayName("공지글 게시글로 변환")
    void convertAsPost() {
        //given
        PostRequest editRequest = new PostRequest("title edit", "content edit", true);
        announcementService.createAnnouncement(userDetails[1], 1L, request);

        //when
        PostResponse response = announcementService.convertAsPost(userDetails[1], 1L, 1L, editRequest);

        //then
        Assertions.assertEquals(editRequest.getTitle(), response.getTitle());
        Assertions.assertEquals(editRequest.getContent(), response.getContent());
        Assertions.assertEquals(false, response.getIsAnnouncement());
    }

    @Test
    @DisplayName("공지글 고정")
    void pinAnnouncement() {
        //given
        announcementService.createAnnouncement(userDetails[0], 1L, request);
        announcementService.createAnnouncement(userDetails[0], 1L, request);
        announcementService.createAnnouncement(userDetails[0], 1L, request);
        announcementService.createAnnouncement(userDetails[0], 1L, request);

        //when
        announcementService.pinAnnouncement(userDetails[0], 1L, 1L, true);
        announcementService.pinAnnouncement(userDetails[0], 1L, 2L, true);
        announcementService.pinAnnouncement(userDetails[0], 1L, 3L, true);

        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> announcementService.pinAnnouncement(userDetails[0], 1L, 4L, true));
        announcementService.pinAnnouncement(userDetails[0], 1L, 3L, false);
        Assertions.assertDoesNotThrow(() -> announcementService.pinAnnouncement(userDetails[0], 1L, 4L, true));

    }

    @Test
    @DisplayName("팀 단위 공지글 조회")
    void getAnnouncementsByTeam() {
        //given
        announcementService.createAnnouncement(userDetails[0], 1L, request);
        announcementService.createAnnouncement(userDetails[0], 1L, request);
        announcementService.createAnnouncement(userDetails[0], 1L, request);

        //when
        announcementService.pinAnnouncement(userDetails[0], 1L, 1L, true);

        Page<AnnouncementResponse> response = announcementService.getAnnouncementsByTeam(userDetails[0], 1L, 1);

        //then
        Assertions.assertEquals(3, response.getTotalElements());
        Assertions.assertEquals(3, response.getContent().size());
        Assertions.assertEquals(1L, response.getContent().get(0).getId());
    }

    @Test
    @DisplayName("유저 단위 읽지 않은 공지글 조회") //todo
    void getUnreadAnnouncementsByUser() {
        //given
        CreateTeamRequest createTeamRequest = new CreateTeamRequest("team2", "test", "red", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), null, null, null, null);
        teamService.createTeam(userDetails[1], createTeamRequest);
        announcementService.createAnnouncement(userDetails[1], 1L, request);
        announcementService.createAnnouncement(userDetails[1], 1L, request);
        announcementService.createAnnouncement(userDetails[1], 1L, request);
        announcementService.createAnnouncement(userDetails[1], 2L, request);

        //when
        announcementService.markAsRead(userDetails[0], 1L);
        List<AnnouncementResponse> response1 = announcementService.getUnreadAnnouncementsByUser(userDetails[0]);

        //then
        Assertions.assertEquals(2, response1.size());
    }

    @Test
    @DisplayName("팀 단위 읽지 않은 공지글 조회")
    void getUnreadAnnouncementsByTeam() {
        //given
        CreateTeamRequest createTeamRequest = new CreateTeamRequest("team2", "test", "red", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), null, null, null, null);
        teamService.createTeam(userDetails[1], createTeamRequest);
        announcementService.createAnnouncement(userDetails[1], 1L, request);
        announcementService.createAnnouncement(userDetails[1], 1L, request);
        announcementService.createAnnouncement(userDetails[1], 1L, request);
        announcementService.createAnnouncement(userDetails[1], 2L, request);

        //when
        announcementService.markAsRead(userDetails[0], 1L);
        announcementService.markAsRead(userDetails[0], 2L);
        announcementService.pinAnnouncement(userDetails[0], 1L, 2L, true);
        List<AnnouncementResponse> response = announcementService.getUnreadAnnouncementsByTeam(userDetails[0],  1L);

        //then
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(2L, response.get(0).getId());
    }


}
