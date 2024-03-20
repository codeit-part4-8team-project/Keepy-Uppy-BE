package com.keepyuppy.KeepyUppy.post.service;

import com.keepyuppy.KeepyUppy.global.exception.AccessDeniedException;
import com.keepyuppy.KeepyUppy.global.exception.NotFoundException;
import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.post.domain.entity.Announcement;
import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.post.repository.AnnouncementJPARepository;
import com.keepyuppy.KeepyUppy.post.repository.PostJpaRepository;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final AnnouncementJPARepository announcementJPARepository;
    private final MemberRepositoryImpl memberRepository;

    @Transactional
    public PostResponse createPost(
            CustomUserDetails userDetails,
            Long teamId,
            PostRequest request
    ){
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

    public PostResponse viewPost(CustomUserDetails userDetails, Long teamId, Long postId){
        getMemberInTeam(userDetails.getUserId(), teamId);
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(NotFoundException.PostNotFoundException::new);
        return PostResponse.of(post);
    }

    @Transactional
    public void deletePost(CustomUserDetails userDetails, Long teamId, Long postId){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(NotFoundException.PostNotFoundException::new);
        Member author = post.getAuthor();

        if (member.getGrade() == Grade.TEAM_MEMBER && !Objects.equals(member.getId(), author.getId())){
            throw new AccessDeniedException.ActionAccessDeniedException();
        }
        postJpaRepository.deleteById(postId);
    }

    @Transactional
    public PostResponse updatePost(
            CustomUserDetails userDetails,
            Long teamId,
            Long postId,
            PostRequest request
    ){

        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(NotFoundException.PostNotFoundException::new);
        Member author = post.getAuthor();

        if (!Objects.equals(member.getId(), author.getId())){
            throw new AccessDeniedException.ActionAccessDeniedException();
        }
        post.update(request);
        return PostResponse.of(postJpaRepository.save(post));
    }

    @Transactional
    public AnnouncementResponse convertAsAnnouncement(
            CustomUserDetails userDetails,
            Long teamId,
            Long postId,
            PostRequest request
    ){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(NotFoundException.PostNotFoundException::new);
        Member author = post.getAuthor();

        if (!Objects.equals(member.getId(), author.getId())){
            throw new AccessDeniedException.ActionAccessDeniedException();
        }
        post.update(request);
        Announcement announcement = post.convertAsAnnouncement();

        postJpaRepository.deleteById(postId);
        return AnnouncementResponse.of(announcementJPARepository.save(announcement));
    }

    // sorted by created date (newer posts on top)
    public Page<PostResponse> getPostPaginate(
            CustomUserDetails userDetails,
            Long teamId,
            int page) {

        Member member = getMemberInTeam(userDetails.getUserId(), teamId);

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Post> posts = postJpaRepository.findByTeamAndTypeOrderByCreatedDateDesc(member.getTeam(), ContentType.POST, pageable);

        return posts.map(PostResponse::of);
    }

    public Member getMemberInTeam(Long userId, Long teamId){
        return memberRepository.findMemberInTeamByUserId(userId, teamId)
                .orElseThrow(AccessDeniedException.TeamAccessDeniedException::new);
    }


}
