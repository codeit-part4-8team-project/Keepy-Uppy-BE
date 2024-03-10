package com.keepyuppy.KeepyUppy.member.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.communication.request.UpdateMemberRequest;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Role;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
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

    public void setTeam(Team team) {
        this.team = team;
    }


    public Member(Users user, Team team, Grade grade, Status status) {
        this.user = user;
        this.team = team;
        this.grade = grade;
        this.status = status;
    }

    public boolean update(Member updater, UpdateMemberRequest updateMemberRequest) {
        if (canUpdate(updater)) {
            this.grade = Grade.getInstance(updateMemberRequest.getGrade());
            this.role = Role.getInstance(updateMemberRequest.getRole());
            return true;
        }

        return false;
    }

    private boolean canUpdate(Member updater) {
        if (updater.getGrade().equals(Grade.OWNER)) {
            return true;
        }

        if (updater.getGrade().equals(Grade.MANAGER) && this.getGrade().equals(Grade.TEAM_MEMBER)) {
            return true;
        }
        if (updater.equals(this)) {
            return true;
        }

        return false;
    }

    public void setUsers(Users user) {
        this.user = user;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}


