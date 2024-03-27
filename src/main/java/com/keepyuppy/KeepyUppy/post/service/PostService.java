package com.keepyuppy.KeepyUppy.post.service;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.global.exception.ExceptionType;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.post.domain.entity.Announcement;
import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.entity.PostLike;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.post.repository.AnnouncementJPARepository;
import com.keepyuppy.KeepyUppy.post.repository.PostJpaRepository;
import com.keepyuppy.KeepyUppy.post.repository.PostLikeJpaRepository;
import com.keepyuppy.KeepyUppy.post.repository.PostRepositoryImpl;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final PostRepositoryImpl postRepository;
    private final AnnouncementJPARepository announcementJPARepository;
    private final MemberRepositoryImpl memberRepository;
    private final PostLikeJpaRepository postLikeJpaRepository;

    @Transactional
    public PostResponse createPost(
            CustomUserDetails userDetails,
            Long teamId,
            PostRequest request
    ) {
        Member author = getMemberInTeam(userDetails.getUserId(), teamId);
        Team team = author.getTeam();

        Post post = Post.builder()
                .title(request.getTitle())
                .author(author)
                .content(request.getContent())
                .type(ContentType.POST)
                .team(team)
                .build();

        return PostResponse.of(postJpaRepository.save(post));
    }

    public PostResponse viewPost(CustomUserDetails userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_FOUND));
        getMemberInTeam(userDetails.getUserId(), post.getTeam().getId());
        return PostResponse.of(post);
    }

    @Transactional
    public void deletePost(CustomUserDetails userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), post.getTeam().getId());
        Member author = post.getAuthor();

        if (member.getGrade() == Grade.TEAM_MEMBER && !Objects.equals(member.getId(), author.getId())) {
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
        postJpaRepository.deleteById(postId);
    }

    @Transactional
    public PostResponse updatePost(
            CustomUserDetails userDetails,
            Long postId,
            PostRequest request
    ) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), post.getTeam().getId());
        Member author = post.getAuthor();

        if (!Objects.equals(member.getId(), author.getId())) {
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
        post.update(request);
        return PostResponse.of(postJpaRepository.save(post));
    }

    @Transactional
    public AnnouncementResponse convertAsAnnouncement(
            CustomUserDetails userDetails,
            Long postId,
            PostRequest request
    ) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), post.getTeam().getId());
        Member author = post.getAuthor();

        if (!Objects.equals(member.getId(), author.getId())) {
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
        post.update(request);
        Announcement announcement = post.convertAsAnnouncement();

        postJpaRepository.deleteById(postId);
        return AnnouncementResponse.of(announcementJPARepository.save(announcement));
    }

    // sorted by created date (newer posts on top)
    public Page<PostResponse> getPostPaginateByTeam(
            CustomUserDetails userDetails,
            Long teamId,
            int page) {

        Member member = getMemberInTeam(userDetails.getUserId(), teamId);

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Post> posts = postJpaRepository.findByTeamAndTypeOrderByCreatedDateDesc(member.getTeam(), ContentType.POST, pageable);

        return posts.map(PostResponse::of);
    }

    // sorted by created date (newer posts on top)
    public Page<PostResponse> getPostPaginateByUser(
            CustomUserDetails userDetails,
            int page) {

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Post> posts = postRepository.findByUserId(userDetails.getUserId(), ContentType.POST, pageable);

        return posts.map(PostResponse::of);
    }

    // sorted by created date (newer posts on top)
    public Page<PostResponse> getPostPaginateFilter(
            CustomUserDetails userDetails,
            List<Long> teamIds,
            int page) {

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Post> posts = postRepository.findByUserIdAndTeams(userDetails.getUserId(), teamIds, ContentType.POST, pageable);

        return posts.map(PostResponse::of);
    }

    private Member getMemberInTeam(Long userId, Long teamId) {
        return memberRepository.findMemberInTeamByUserId(userId, teamId)
                .orElseThrow(() -> new CustomException(ExceptionType.TEAM_ACCESS_DENIED));
    }

    @Transactional
    public PostResponse likePost(CustomUserDetails userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), post.getTeam().getId());

        if (post.getLikes().stream().map(PostLike::getMember).anyMatch(memberInList -> memberInList.getId().equals(member.getId()))) {
            throw new CustomException(ExceptionType.ALREADY_LIKE_POST);
        }

        PostLike postLike = new PostLike(member, post);
        postLikeJpaRepository.save(postLike);

        post.addLike(postLike);

        return PostResponse.of(post);
    }

    @Transactional
    public PostResponse unlikePost(CustomUserDetails userDetails, Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionType.POST_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), post.getTeam().getId());

        PostLike postLike = postLikeJpaRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new CustomException(ExceptionType.LIKE_NOT_FOUND));

        postLikeJpaRepository.delete(postLike);
        post.removeLike(postLike);

        return PostResponse.of(post);
    }

}
