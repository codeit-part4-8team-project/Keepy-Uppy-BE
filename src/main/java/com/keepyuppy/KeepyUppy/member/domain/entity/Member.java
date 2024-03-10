package com.keepyuppy.KeepyUppy.member.domain.entity;

import com.keepyuppy.KeepyUppy.content.domain.entity.IssueAssignment;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Role;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "member")
    private Set<IssueAssignment> issueAssignments = new HashSet<>();

    public void setTeam(Team team) {
        this.team = team;
    }


    public Member(Users user, Team team, Grade grade, String role, Status status) {
        this.user = user;
        this.team = team;
        this.grade = grade;
        this.role = Role.getInstance(role);
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

