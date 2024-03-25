package com.keepyuppy.KeepyUppy.team.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeamRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String color;
    private Long ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String figma;
    private String github;
    private String discord;

    // fetch is lazy by default
    @OneToMany(mappedBy = "team")
    private Set<Member> members = new HashSet<>();


    @Builder
    public Team(String name, String description, String color, LocalDate startDate, LocalDate endDate, String figma, String github, String discord) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.startDate = startDate;
        this.endDate = endDate;
        this.figma = figma;
        this.github = github;
        this.discord = discord;
    }

    public void addMember(Member member) {
        member.setTeam(this);
        this.members.add(member);
    }

    public void removeMember(Member member) {
        member.setTeam(null);
        this.members.remove(member);
    }


    public void update(UpdateTeamRequest updateTeamRequest) {
        this.name = updateTeamRequest.getName();
        this.description = updateTeamRequest.getDescription();
        this.color = updateTeamRequest.getColor();
        this.startDate = updateTeamRequest.getStartDate();
        this.endDate = updateTeamRequest.getEndDate();
        this.figma = updateTeamRequest.getFigmaLink();
        this.github = updateTeamRequest.getGithubLink();
        this.discord = updateTeamRequest.getDiscordLink();
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}