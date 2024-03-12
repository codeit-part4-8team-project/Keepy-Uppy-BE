package com.keepyuppy.KeepyUppy.content.service;

import com.keepyuppy.KeepyUppy.content.communication.request.IssueRequest;
import com.keepyuppy.KeepyUppy.content.communication.request.IssueStatusRequest;
import com.keepyuppy.KeepyUppy.content.communication.response.IssueResponse;
import com.keepyuppy.KeepyUppy.content.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.content.domain.entity.IssueAssignment;
import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.content.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.content.repository.IssueAssignmentJpaRepository;
import com.keepyuppy.KeepyUppy.content.repository.IssueJpaRepository;
import com.keepyuppy.KeepyUppy.global.exception.AccessDeniedException;
import com.keepyuppy.KeepyUppy.global.exception.NotFoundException;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueJpaRepository issueJpaRepository;
    private final MemberRepositoryImpl memberRepository;
    private final IssueAssignmentJpaRepository assignmentJpaRepository;

    @Transactional
    public IssueResponse createIssue(
            CustomUserDetails userDetails,
            Long teamId,
            IssueRequest request
    ){
        Member author = getMemberInTeam(userDetails.getUserId(), teamId);
        Team team = author.getTeam();
        IssueStatus type = request.getStatus() == null ? IssueStatus.TODO : request.getStatus();

        Issue issue = Issue.issueBuilder()
                .title(request.getTitle())
                .author(author)
                .content(request.getContent())
                .type(ContentType.ISSUE)
                .dueDate(request.getDueDate())
                .status(type)
                .build();

        assignMembers(issue, request.getAssignedMembersUsernames(), teamId);

        // check if the changes persist
        team.addContent(issue);
        issueJpaRepository.save(issue);
        return IssueResponse.of(issue);
    }

    public IssueResponse viewIssue(CustomUserDetails userDetails, Long teamId, Long issueId){
        getMemberInTeam(userDetails.getUserId(), teamId);
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 이슈입니다."));
        return IssueResponse.of(issue);
    }

    @Transactional
    public void deleteIssue(CustomUserDetails userDetails, Long teamId, Long issueId){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 이슈입니다."));
        Member author = issue.getAuthor();

        if (member.getGrade() == Grade.TEAM_MEMBER && !Objects.equals(member.getId(), author.getId())){
            throw new AccessDeniedException("삭제할 권한이 없는 이슈입니다.");
        }

        deleteAssignments(issue);
        issueJpaRepository.deleteById(issueId);
    }

    @Transactional
    public IssueResponse updateIssue(
            CustomUserDetails userDetails,
            Long teamId,
            Long issueId,
            IssueRequest request
    ){

        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 이슈입니다."));

        if (!Objects.equals(member.getId(), issue.getAuthor().getId())){
            throw new AccessDeniedException("수정할 권한이 없는 이슈입니다.");
        }

        issue.update(request);
        List<String> usernames = request.getAssignedMembersUsernames();
        if (usernames != null){
            // reset issueAssignment before updating
            deleteAssignments(issue);
            assignMembers(issue, request.getAssignedMembersUsernames(), teamId);
        }

        issueJpaRepository.save(issue);
        return IssueResponse.of(issue);
    }

    public Member getMemberInTeam(Long userId, Long teamId){
        return memberRepository.findMemberInTeamByUserId(userId, teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 속하지 않은 팀입니다."));
    }

    @Transactional
    public IssueResponse updateStatus(
            CustomUserDetails userDetails,
            Long teamId,
            Long issueId,
            IssueStatusRequest request
    ){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 이슈입니다."));

        // check if the member is an author or has the issue assigned
        // also allow update if there were no assignments
        boolean isAuthor = Objects.equals(member.getId(), issue.getAuthor().getId());
        boolean isAssigned = issue.getIssueAssignments().stream()
                .map(assignment -> assignment.getMember().getUser().getId())
                .collect(Collectors.toSet()).contains(userDetails.getUserId());
        isAssigned = issue.getIssueAssignments().isEmpty() || isAssigned;

        if ( !isAuthor && !isAssigned ){
            throw new AccessDeniedException("수정할 권한이 없는 이슈입니다.");
        }

        issue.updateStatus(request);
        issueJpaRepository.save(issue);
        return IssueResponse.of(issue);
    }

    @Transactional
    public Set<IssueAssignment> assignMembers(Issue issue, List<String> usernames, Long teamId){
        // retrieve members to assign
        Set<Member> members = usernames.stream()
                .map(username -> memberRepository.findMemberInTeamByUsername(username, teamId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        Set<IssueAssignment> issueAssignments = members.stream()
                .map(member -> IssueAssignment.builder().issue(issue).member(member).build())
                .collect(Collectors.toSet());

        // update assignment in members and issue
        issueAssignments.forEach(assignment -> assignment.getMember().addIssueAssignment(assignment));
        issue.setAssignments(issueAssignments);

        assignmentJpaRepository.saveAll(issueAssignments);
        return issueAssignments;
    }

    @Transactional
    public void deleteAssignments(Issue issue){
        issue.getIssueAssignments().forEach(assignment -> {
            assignment.getMember().removeIssueAssignment(assignment);
            assignmentJpaRepository.deleteById(assignment.getId());
        });
    }


}
