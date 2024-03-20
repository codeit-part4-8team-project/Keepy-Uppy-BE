package com.keepyuppy.KeepyUppy.issue.service;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.global.exception.ExceptionType;
import com.keepyuppy.KeepyUppy.issue.communication.request.IssueRequest;
import com.keepyuppy.KeepyUppy.issue.communication.request.IssueStatusRequest;
import com.keepyuppy.KeepyUppy.issue.communication.response.IssueBoardResponse;
import com.keepyuppy.KeepyUppy.issue.communication.response.IssueResponse;
import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.issue.domain.entity.IssueAssignment;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.issue.repository.IssueAssignmentJpaRepository;
import com.keepyuppy.KeepyUppy.issue.repository.IssueJpaRepository;
import com.keepyuppy.KeepyUppy.issue.repository.IssueRepositoryImpl;
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
    private final IssueRepositoryImpl issueRepository;
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
                .team(team)
                .status(type)
                .build();

        issueJpaRepository.save(issue);

        if (request.getAssignedMembersUsernames() != null){
            assignMembers(issue, request.getAssignedMembersUsernames(), teamId);
        }

        return IssueResponse.of(issue);
    }

    public IssueResponse viewIssue(CustomUserDetails userDetails, Long teamId, Long issueId){
        getMemberInTeam(userDetails.getUserId(), teamId);
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new CustomException(ExceptionType.ISSUE_NOT_FOUND));
        return IssueResponse.of(issue);
    }

    @Transactional
    public void deleteIssue(CustomUserDetails userDetails, Long teamId, Long issueId){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new CustomException(ExceptionType.ISSUE_NOT_FOUND));
        Member author = issue.getAuthor();

        if (member.getGrade() == Grade.TEAM_MEMBER && !Objects.equals(member.getId(), author.getId())){
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
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
                .orElseThrow(() -> new CustomException(ExceptionType.ISSUE_NOT_FOUND));

        if (!Objects.equals(member.getId(), issue.getAuthor().getId())){
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }

        issue.update(request);
        issue = issueJpaRepository.save(issue);

        if (request.getAssignedMembersUsernames() != null){
            // reset issueAssignment before updating
            deleteAssignments(issue);
            assignMembers(issue, request.getAssignedMembersUsernames(), teamId);
        }

        return IssueResponse.of(issue);
    }

    public Member getMemberInTeam(Long userId, Long teamId){
        return memberRepository.findMemberInTeamByUserId(userId, teamId)
                .orElseThrow(() -> new CustomException(ExceptionType.TEAM_ACCESS_DENIED));
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
                .orElseThrow(() -> new CustomException(ExceptionType.ISSUE_NOT_FOUND));

        // check if the member is an author or has the issue assigned
        // also allow update if there were no assignments
        boolean isAuthor = Objects.equals(member.getId(), issue.getAuthor().getId());
        boolean isAssigned = issue.getIssueAssignments().stream()
                .map(assignment -> assignment.getMember().getUser().getId())
                .collect(Collectors.toSet()).contains(userDetails.getUserId());
        isAssigned = issue.getIssueAssignments().isEmpty() || isAssigned;

        if ( !isAuthor && !isAssigned ){
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }

        issue.updateStatus(request);
        return IssueResponse.of(issueJpaRepository.save(issue));
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

    public IssueBoardResponse getTeamIssueBoard(CustomUserDetails userDetails, Long teamId){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);

        List<Issue> todo = issueJpaRepository.findByTeamAndStatusOrderByModifiedDateAsc(member.getTeam(), IssueStatus.TODO);
        List<Issue> progress = issueJpaRepository.findByTeamAndStatusOrderByModifiedDateAsc(member.getTeam(), IssueStatus.INPROGRESS);
        List<Issue> done = issueJpaRepository.findByTeamAndStatusOrderByModifiedDateAsc(member.getTeam(), IssueStatus.DONE);

        return IssueBoardResponse.of(todo, progress, done);
    }

    public IssueBoardResponse getMyIssueBoard(CustomUserDetails userDetails){
        List<Issue> todo = issueRepository.findByAssignedUserId(userDetails.getUserId(), IssueStatus.TODO);
        List<Issue> progress = issueRepository.findByAssignedUserId(userDetails.getUserId(), IssueStatus.INPROGRESS);
        List<Issue> done = issueRepository.findByAssignedUserId(userDetails.getUserId(), IssueStatus.DONE);

        return IssueBoardResponse.of(todo, progress, done);
    }

}
