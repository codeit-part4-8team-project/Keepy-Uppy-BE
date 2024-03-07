package com.keepyuppy.KeepyUppy.member.repository;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.user.domain.entity.QUsers;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

    public Optional<Member> findInviteByUserId(Long userId,Long teamId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(member)
                        .join(member.user, QUsers.users)
                        .where(member.user.id.eq(userId))
                        .where(member.team.id.eq(teamId))
                        .where(member.status.eq(Status.PENDING))
                        .fetchOne()
        );
    }
}
