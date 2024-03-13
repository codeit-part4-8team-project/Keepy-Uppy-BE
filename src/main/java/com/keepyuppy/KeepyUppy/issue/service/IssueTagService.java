package com.keepyuppy.KeepyUppy.issue.service;

import com.keepyuppy.KeepyUppy.issue.communication.request.IssueTagRequest;
import com.keepyuppy.KeepyUppy.issue.communication.response.IssueTagResponse;
import com.keepyuppy.KeepyUppy.issue.domain.entity.IssueTag;
import com.keepyuppy.KeepyUppy.issue.repository.IssueTagJpaRepository;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class IssueTagService {

    private final MemberRepositoryImpl memberRepository;
    private final IssueTagJpaRepository tagJpaRepository;

    @Transactional
    public IssueTagResponse createTag(
            CustomUserDetails userDetails,
            Long teamId, IssueTagRequest request){

        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        String color = request.getColor() == null ? "#808080" : request.getColor();

        validateTagName(request.getName());
        IssueTag tag = IssueTag.builder()
                .team(member.getTeam())
                .name(request.getName())
                .color(color)
                .build();

        tagJpaRepository.save(tag);
        return IssueTagResponse.of(tag);
    }

    public Member getMemberInTeam(Long userId, Long teamId){
        return memberRepository.findMemberInTeamByUserId(userId, teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 속하지 않은 팀입니다."));
    }

    public void validateTagName(String tagName){
        if (tagName == null) {
            return;
        } else if (tagName.length() < 1 || tagName.length() > 30) {
            throw new IllegalArgumentException("태그는 1자 이상, 30자 이하여야 합니다.");
        } else if (tagJpaRepository.findByName(tagName).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 유저네임입니다.");
        }
    }

    public List<IssueTagResponse> getTeamTags(CustomUserDetails userDetails, Long teamId){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);

        List<IssueTag> tags = tagJpaRepository.findByTeam(member.getTeam());
        return tags.stream().map(IssueTagResponse::of).toList();
    }

    @Transactional
    public void deleteTag(CustomUserDetails userDetails,
                          Long teamId, String tagName) {
        getMemberInTeam(userDetails.getUserId(), teamId);
        IssueTag tag = tagJpaRepository.findByName(tagName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그입니다."));
        tagJpaRepository.delete(tag);
    }




}
