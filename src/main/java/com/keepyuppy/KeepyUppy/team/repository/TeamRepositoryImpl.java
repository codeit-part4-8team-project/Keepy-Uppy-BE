package com.keepyuppy.KeepyUppy.team.repository;

import com.keepyuppy.KeepyUppy.member.domain.entity.QMember;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.team.domain.entity.QTeam;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.domain.enums.Type;
import com.keepyuppy.KeepyUppy.user.domain.entity.QUsers;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Team> findProjectByUsersId(Long id) {
        return jpaQueryFactory.select(QTeam.team)
                .from(QMember.member)
                .join(QMember.member.user, QUsers.users)
                .join(QMember.member.team, QTeam.team)
                .where(QUsers.users.id.eq(id))
                .where(QMember.member.status.eq(Status.ACCEPTED))
                .where(QTeam.team.type.eq(Type.PROJECT))
                .fetch();
    }

    public List<Team> findInvitedTeamByUsersId(Long id) {
        return jpaQueryFactory.select(QTeam.team)
                .from(QMember.member)
                .join(QMember.member.user, QUsers.users)
                .join(QMember.member.team, QTeam.team)
                .where(QUsers.users.id.eq(id))
                .where(QMember.member.status.eq(Status.PENDING))
                .where(QTeam.team.type.eq(Type.PROJECT))
                .fetch();
    }




}
