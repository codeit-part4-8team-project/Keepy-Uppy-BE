package com.keepyuppy.KeepyUppy.member.repository;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.team.domain.entity.QTeam;
import com.keepyuppy.KeepyUppy.user.domain.entity.QUsers;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.keepyuppy.KeepyUppy.member.domain.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Member> findByUserId(Long userId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(member)
                .join(member.user, QUsers.users)
                .where(member.user.id.eq(userId))
                .fetchOne());
    }

    public Optional<Member> findPendingByUserId(Long userId, Long teamId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(member)
                        .join(member.user, QUsers.users)
                        .where(member.user.id.eq(userId))
                        .where(member.team.id.eq(teamId))
                        .where(member.status.eq(Status.PENDING))
                        .fetchOne()
        );
    }

    public Optional<Member> findMemberInTeamByUserId(Long userId, Long teamId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(member)
                        .where(
                                member.user.id.eq(userId)
                                        .and(member.team.id.eq(teamId))
                        )
                        .fetchOne()
        );
    }

    public Optional<List<Member>> findMembersByTeamId(Long teamId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(member)
                        .join(member.team, QTeam.team)
                        .where(member.team.id.eq(teamId))
                        .fetch()
        );
    }

    public Optional<Member> findMemberInTeamByUsername(String username, Long teamId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(member)
                        .where(
                                member.user.username.eq(username)
                                        .and(member.team.id.eq(teamId))
                        )
                        .fetchOne()
        );
    }

    public List<Member> findMemberInTeamByUsernamePattern(String username, Long teamId) {
        return jpaQueryFactory.selectFrom(member)
                .join(member.user, QUsers.users)
                .fetchJoin()
                .where(member.user.username.lower().like("%" + username + "%")
                        .and(member.team.id.eq(teamId)))
                .fetch();
    }
}



