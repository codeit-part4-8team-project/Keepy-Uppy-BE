package com.keepyuppy.KeepyUppy;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.issue.communication.request.IssueRequest;
import com.keepyuppy.KeepyUppy.issue.communication.request.IssueStatusRequest;
import com.keepyuppy.KeepyUppy.issue.communication.response.IssueResponse;
import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.issue.service.IssueService;
import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeamRequest;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import com.keepyuppy.KeepyUppy.user.communication.request.UpdateUserRequest;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import com.keepyuppy.KeepyUppy.user.service.UserService;
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
public class IssueTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @Autowired
    IssueService issueService;

    private Users[] users;
    private CustomUserDetails[] userDetails;
    private IssueRequest request;

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

        request = new IssueRequest("title", "content", LocalDate.of(2024, 1, 1), IssueStatus.TODO, List.of("username0"));
    }

    @Test
    @DisplayName("이슈 생성")
    void createIssue() {
        //when
        IssueResponse response = issueService.createIssue(userDetails[0], 1L, request);

        //then
        Assertions.assertEquals(request.getTitle(), response.getTitle());
        Assertions.assertEquals(request.getContent(), response.getContent());
        Assertions.assertEquals(1L, response.getTeam().getId());
        Assertions.assertEquals(users[0].getName(), response.getAuthor().getName());
        Assertions.assertEquals(1, response.getAssignedMembers().size());
    }

    @Test
    @DisplayName("이슈 조회")
    void viewIssue() {
        issueService.createIssue(userDetails[0], 1L, request);

        //when
        teamService.updateTeam(userDetails[0], 1L, new UpdateTeamRequest("edit", null, null, null, null, null, null, null));
        userService.updateUser(1L, new UpdateUserRequest("new name", null, null));
        IssueResponse response = issueService.viewIssue(userDetails[0], 1L);


        //then
        Assertions.assertEquals(request.getTitle(), response.getTitle());
        Assertions.assertEquals(request.getContent(), response.getContent());
        Assertions.assertEquals("edit", response.getTeam().getName());
        Assertions.assertEquals("new name", response.getAuthor().getName());
        Assertions.assertEquals(1, response.getAssignedMembers().size());
        Assertions.assertEquals("new name", response.getAssignedMembers().get(0).getName());
    }

    @Test
    @DisplayName("이슈 삭제")
    void deleteIssue() {
        //given
        issueService.createIssue(userDetails[1], 1L, request);
        issueService.createIssue(userDetails[1], 1L, request);

        //when
        issueService.deleteIssue(userDetails[1], 1L);
        issueService.deleteIssue(userDetails[0], 2L);

        //then
        Assertions.assertThrows(CustomException.class, () -> issueService.viewIssue(userDetails[0], 1L));
        Assertions.assertThrows(CustomException.class, () -> issueService.viewIssue(userDetails[0], 2L));
    }

    @Test
    @DisplayName("이슈 삭제 권한이 없는 경우")
    void deletePostFailure() {
        //given
        issueService.createIssue(userDetails[0], 1L, request);

        //then
        Assertions.assertThrows(CustomException.class, () -> issueService.deleteIssue(userDetails[1], 1L));
    }

    @Test
    @DisplayName("본인 이슈 수정")
    void updateIssueByAuthor() {
        //given
        IssueRequest editRequest = new IssueRequest("title edit", "content edit", null, null, null);
        issueService.createIssue(userDetails[0], 1L, request);

        //when
        IssueResponse response = issueService.updateIssue(userDetails[0], 1L, editRequest);

        //then
        Assertions.assertEquals(editRequest.getTitle(), response.getTitle());
        Assertions.assertEquals(editRequest.getContent(), response.getContent());
        Assertions.assertEquals(1, response.getAssignedMembers().size());
    }

    @Test
    @DisplayName("이슈 수정 권한이 없는 경우")
    void updatePostFailure() {
        //given
        issueService.createIssue(userDetails[1], 1L, request);

        //then
        Assertions.assertThrows(CustomException.class, () -> issueService.updateIssue(userDetails[0], 1L, request));
    }

    @Test
    @DisplayName("이슈 담당 수정")
    void updateIssueAssignments() {
        //given
        IssueRequest editRequest = new IssueRequest("title edit", "content edit", null, null, List.of("username1"));
        issueService.createIssue(userDetails[0], 1L, request);

        //when
        IssueResponse response = issueService.updateIssue(userDetails[0],  1L, editRequest);

        //then
        Assertions.assertEquals(editRequest.getTitle(), response.getTitle());
        Assertions.assertEquals(editRequest.getContent(), response.getContent());
        Assertions.assertEquals(1, response.getAssignedMembers().size());
        Assertions.assertEquals(users[1].getName(), response.getAssignedMembers().get(0).getName());
    }

    @Test
    @DisplayName("작성자가 이슈 상태 수정")
    void updateIssueStatusByAuthor() {
        //given
        IssueStatusRequest editRequest = new IssueStatusRequest(IssueStatus.DONE);
        issueService.createIssue(userDetails[0], 1L, request);

        //when
        IssueResponse response = issueService.updateStatus(userDetails[0], 1L, editRequest);

        //then
        Assertions.assertEquals(request.getTitle(), response.getTitle());
        Assertions.assertEquals(editRequest.getStatus(), response.getStatus());
        Assertions.assertThrows(CustomException.class, () -> issueService.updateStatus(userDetails[1], 1L, editRequest));
    }

    @Test
    @DisplayName("담당 멤버가 이슈 상태 수정")
    void updateIssueStatusByAssigned() {
        //given
        IssueRequest newRequest = new IssueRequest("title edit", "content edit", null, null, List.of("username1"));
        IssueStatusRequest editRequest = new IssueStatusRequest(IssueStatus.DONE);
        issueService.createIssue(userDetails[0], 1L, newRequest);

        //when
        IssueResponse response = issueService.updateStatus(userDetails[1], 1L, editRequest);

        //then
        Assertions.assertEquals(newRequest.getTitle(), response.getTitle());
        Assertions.assertEquals(editRequest.getStatus(), response.getStatus());
    }

    // todo add tests for getIssueBoardMethods (method needs discussion)


}
