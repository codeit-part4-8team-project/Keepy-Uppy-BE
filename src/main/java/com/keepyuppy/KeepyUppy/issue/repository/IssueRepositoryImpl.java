package com.keepyuppy.KeepyUppy.issue.repository;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.keepyuppy.KeepyUppy.issue.domain.entity.QIssue.issue;
import static com.keepyuppy.KeepyUppy.issue.domain.entity.QIssueAssignment.issueAssignment;
import static com.keepyuppy.KeepyUppy.member.domain.entity.QMember.member;
import static com.keepyuppy.KeepyUppy.team.domain.entity.QTeam.team;
import static com.keepyuppy.KeepyUppy.user.domain.entity.QUsers.users;

@Repository
@RequiredArgsConstructor
public class IssueRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public List<Issue> findByAssignedUserId(Long userId, IssueStatus status) {
        return queryFactory.selectFrom(issue)
                .innerJoin(issue.issueAssignments, issueAssignment)
                .innerJoin(issueAssignment.member, member)
                .innerJoin(member.user, users)
                .innerJoin(issue.team, team)
                .where(users.id.eq(userId).and(issue.status.eq(status)))
                .orderBy(issue.modifiedDate.asc())
                .fetch();
    }

}
