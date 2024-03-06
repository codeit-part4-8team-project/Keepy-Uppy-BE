package com.keepyuppy.KeepyUppy.organization.repository;

import com.keepyuppy.KeepyUppy.member.domain.entity.QMember;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.organization.domain.entity.Organization;
import com.keepyuppy.KeepyUppy.organization.domain.entity.QOrganization;
import com.keepyuppy.KeepyUppy.organization.domain.enums.Type;
import com.keepyuppy.KeepyUppy.user.domain.entity.QUsers;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Organization> findTeamByUsersId(Long id) {
        return jpaQueryFactory.select(QOrganization.organization)
                .from(QMember.member)
                .join(QMember.member.users, QUsers.users)
                .join(QMember.member.organization, QOrganization.organization)
                .where(QUsers.users.id.eq(id))
                .where(QMember.member.status.eq(Status.ACCEPTED))
                .where(QOrganization.organization.type.eq(Type.TEAM))
                .fetch();
    }

    public List<Organization> findInvitedTeamByUsersId(Long id) {
        return jpaQueryFactory.select(QOrganization.organization)
                .from(QMember.member)
                .join(QMember.member.users, QUsers.users)
                .join(QMember.member.organization, QOrganization.organization)
                .where(QUsers.users.id.eq(id))
                .where(QMember.member.status.eq(Status.PENDING))
                .where(QOrganization.organization.type.eq(Type.TEAM))
                .fetch();
    }




}
