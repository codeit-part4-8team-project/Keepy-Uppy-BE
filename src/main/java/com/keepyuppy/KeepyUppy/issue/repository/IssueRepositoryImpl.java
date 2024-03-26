package com.keepyuppy.KeepyUppy.issue.repository;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.keepyuppy.KeepyUppy.issue.domain.entity.QIssue.issue;
import static com.keepyuppy.KeepyUppy.issue.domain.entity.QIssueAssignment.issueAssignment;
import static com.keepyuppy.KeepyUppy.member.domain.entity.QMember.member;
import static com.keepyuppy.KeepyUppy.team.domain.entity.QTeam.team;

@Repository
@RequiredArgsConstructor
public class IssueRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public List<Issue> findByAssignedUserId(Long userId, IssueStatus status) {
        return queryFactory.selectFrom(issue)
                .innerJoin(issue.issueAssignments, issueAssignment).fetchJoin()
                .innerJoin(issueAssignment.member, member).fetchJoin()
                .innerJoin(issue.team, team).fetchJoin()
                .where(issueAssignment.member.user.id.eq(userId).and(issue.status.eq(status)))
                .orderBy(issue.modifiedDate.asc())
                .fetch();
    }

    public List<Issue> findByTeamIdAndStatus(Long teamId, IssueStatus status){
        return queryFactory.selectFrom(issue)
                .innerJoin(issue.issueAssignments, issueAssignment).fetchJoin()
                .innerJoin(issueAssignment.member, member).fetchJoin()
                .innerJoin(issue.team, team).fetchJoin()
                .where(team.id.eq(teamId).and(issue.status.eq(status)))
                .orderBy(issue.modifiedDate.asc())
                .fetch();
    }

    public List<Issue> findByUserIdAndTeams(Long userId, List<Long> teamIds, IssueStatus status) {
        return queryFactory.selectFrom(issue)
                .innerJoin(issue.issueAssignments, issueAssignment).fetchJoin()
                .innerJoin(issueAssignment.member, member).fetchJoin()
                .innerJoin(issue.team, team).fetchJoin()
                .where(issueAssignment.member.user.id.eq(userId)
                        .and(issue.status.eq(status))
                        .and(issue.team.id.in(teamIds)))
                .orderBy(issue.modifiedDate.asc())
                .fetch();
    }

}
