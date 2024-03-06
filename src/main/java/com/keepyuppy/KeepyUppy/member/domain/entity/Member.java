package com.keepyuppy.KeepyUppy.member.domain.entity;

import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Role;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.organization.domain.entity.Organization;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }



    public Member(Users user, Organization organization, Grade grade, Role role, Status status) {
        this.user = user;
        this.organization = organization;
        this.grade = grade;
        this.role = role;
        this.status = status;
    }

    public void changeRole(String role) {
        this.role = Role.getInstance(role);
    }

    public void setUsers(Users user) {
        this.user = user;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
