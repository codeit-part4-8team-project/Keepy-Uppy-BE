package com.keepyuppy.KeepyUppy;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.entity.PostLike;
import com.keepyuppy.KeepyUppy.post.repository.PostJpaRepository;
import com.keepyuppy.KeepyUppy.post.service.PostService;
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
public class PostTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamService teamService;

    @Autowired
    PostService postService;

    @Autowired
    PostJpaRepository postJpaRepository;

    @Autowired
    MemberRepositoryImpl memberRepository;

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

        request = new PostRequest("title", "content");
    }

    @Test
    @DisplayName("게시글 생성")
    void createPost() {
        //when
        PostResponse response = postService.createPost(userDetails[0], 1L, request);

        //then
        Assertions.assertEquals(request.getTitle(), response.getTitle());
        Assertions.assertEquals(request.getContent(), response.getContent());
    }

    @Test
    @DisplayName("게시글 조회")
    void viewPost() {
        postService.createPost(userDetails[0], 1L, request);

        //when
        PostResponse response1 = postService.viewPost(userDetails[0], 1L);
        PostResponse response2 = postService.viewPost(userDetails[1], 1L);

        //then
        Assertions.assertEquals(request.getTitle(), response1.getTitle());
        Assertions.assertEquals(request.getContent(), response1.getContent());
        Assertions.assertEquals(request.getTitle(), response2.getTitle());
        Assertions.assertEquals(request.getContent(), response2.getContent());
    }

    @Test
    @DisplayName("팀에 속하지 않은 유저는 실패")
    void notMember() {
        postService.createPost(userDetails[0], 1L, request);

        //then
        Assertions.assertThrows(CustomException.class, () -> postService.createPost(userDetails[2], 1L, request));
        Assertions.assertThrows(CustomException.class, () -> postService.viewPost(userDetails[2], 1L));
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
        //given
        postService.createPost(userDetails[1], 1L, request);
        postService.createPost(userDetails[1], 1L, request);

        //when
        postService.deletePost(userDetails[1], 1L);
        postService.deletePost(userDetails[0], 2L);

        //then
        Assertions.assertThrows(CustomException.class, () -> postService.viewPost(userDetails[0], 1L));
        Assertions.assertThrows(CustomException.class, () -> postService.viewPost(userDetails[0], 2L));
    }

    @Test
    @DisplayName("게시글 삭제 권한이 없는 경우")
    void deletePostFailure() {
        //given
        postService.createPost(userDetails[0], 1L, request);

        //then
        Assertions.assertThrows(CustomException.class, () -> postService.deletePost(userDetails[1], 1L));
    }

    @Test
    @DisplayName("본인 게시글 수정")
    void updatePostByAuthor() {
        //given
        PostRequest editRequest = new PostRequest("title edit", "content edit");
        postService.createPost(userDetails[1], 1L, request);

        //when
        PostResponse response = postService.updatePost(userDetails[1], 1L, editRequest);

        //then
        Assertions.assertEquals(editRequest.getTitle(), response.getTitle());
        Assertions.assertEquals(editRequest.getContent(), response.getContent());
    }

    @Test
    @DisplayName("게시글 수정 권한이 없는 경우")
    void updatePostFailure() {
        //given
        postService.createPost(userDetails[1], 1L, request);

        //then
        Assertions.assertThrows(CustomException.class, () -> postService.updatePost(userDetails[0], 1L, request));
    }

    @Test
    @DisplayName("게시글 공지글로 변환")
    void convertAsAnnouncement() {
        //given
        PostRequest editRequest = new PostRequest("title edit", "content edit");
        postService.createPost(userDetails[1], 1L, request);

        //when
        AnnouncementResponse response = postService.convertAsAnnouncement(userDetails[1], 1L, editRequest);

        //then
        Assertions.assertEquals(editRequest.getTitle(), response.getTitle());
        Assertions.assertEquals(editRequest.getContent(), response.getContent());
        Assertions.assertEquals(true, response.getIsAnnouncement());
    }

    @Test
    @DisplayName("팀 단위 게시글 조회")
    void getPostPaginateByTeam() {
        //given
        CreateTeamRequest createTeamRequest = new CreateTeamRequest("team2", "test", "red", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), List.of("username1"), null, null, null);
        teamService.createTeam(userDetails[0], createTeamRequest);
        postService.createPost(userDetails[1], 1L, request);
        postService.createPost(userDetails[1], 1L, request);
        postService.createPost(userDetails[1], 1L, request);
        postService.createPost(userDetails[1], 2L, request);

        //when
        Page<PostResponse> response = postService.getPostPaginateByTeam(userDetails[1], 1L, 1);

        //then
        Assertions.assertEquals(3, response.getTotalElements());
        Assertions.assertEquals(3, response.getContent().size());
    }

    @Test
    @DisplayName("유저 단위 게시글 조회")
    void getPostPaginateByUser() {
        //given
        CreateTeamRequest createTeamRequest = new CreateTeamRequest("team2", "test", "red", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), null, null, null, null);
        teamService.createTeam(userDetails[1], createTeamRequest);
        postService.createPost(userDetails[1], 1L, request);
        postService.createPost(userDetails[1], 1L, request);
        postService.createPost(userDetails[1], 1L, request);
        postService.createPost(userDetails[1], 2L, request);

        //when
        Page<PostResponse> response1 = postService.getPostPaginateByUser(userDetails[0],  1);
        Page<PostResponse> response2 = postService.getPostPaginateByUser(userDetails[1],  1);

        //then
        Assertions.assertEquals(3, response1.getTotalElements());
        Assertions.assertEquals(3, response1.getContent().size());
        Assertions.assertEquals(4, response2.getTotalElements());
        Assertions.assertEquals(4, response2.getContent().size());
    }

    @Test
    @DisplayName("게시글에 좋아요")
    void postLike() {
        //given
        postService.createPost(userDetails[0], 1L, request);
        Member likeMember = memberRepository.findMemberInTeamByUserId(1L, 1L).get();

        //when
        postService.likePost(userDetails[0],1L);
        Post post = postJpaRepository.findById(1L).get();

        //then
        Assertions.assertEquals(1L, post.getLikes().size());
        Assertions.assertTrue(post.getLikes().stream().map(PostLike::getMember).toList().contains(likeMember));
        Assertions.assertThrows(CustomException.class, () -> postService.likePost(userDetails[0],1L));
    }

    @Test
    @DisplayName("게시글 좋아요 취소")
    void postUnLike() {
        //given
        postService.createPost(userDetails[0], 1L, request);
        Member likeMember = memberRepository.findMemberInTeamByUserId(1L, 1L).get();
        postService.likePost(userDetails[0],1L);
        Post post = postJpaRepository.findById(1L).get();

        Assertions.assertEquals(1L,post.getLikes().size());

        //when
        postService.unlikePost(userDetails[0], post.getId());

        //then
        Assertions.assertEquals(0,post.getLikes().size());
    }


}
