package com.keepyuppy.KeepyUppy.content.service;

import com.keepyuppy.KeepyUppy.content.communication.request.CreatePostRequest;
import com.keepyuppy.KeepyUppy.content.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.content.domain.entity.Post;
import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.content.repository.PostJpaRepository;
import com.keepyuppy.KeepyUppy.global.exception.NotFoundException;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final MemberRepositoryImpl memberRepository;

    @Transactional
    public PostResponse createPost(CustomUserDetails userDetails, Long teamId, CreatePostRequest request){
        Member author = getMemberInTeam(userDetails.getUserId(), teamId);
        Team team = author.getTeam();
        ContentType type = request.isAnnouncement() ? ContentType.ANNOUNCEMENT : ContentType.POST;

        Post post = Post.builder()
                .title(request.getTitle())
                .author(author)
                .content(request.getContent())
                .type(type)
                .build();

        team.addContent(post);
        postJpaRepository.save(post);

        return PostResponse.of(post);
    }

    public PostResponse viewPost(CustomUserDetails userDetails, Long teamId, Long postId){
        getMemberInTeam(userDetails.getUserId(), teamId);
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시글입니다."));
        return PostResponse.of(post);
    }

    public Member getMemberInTeam(Long userId, Long teamId){
        return memberRepository.findMemberInTeamByUserId(userId, teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 속하지 않은 팀입니다."));
    }


}
