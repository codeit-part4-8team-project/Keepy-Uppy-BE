package com.keepyuppy.KeepyUppy.team.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeam;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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


    @OneToMany(mappedBy = "team")
    private Set<Schedule> schedules = new HashSet<>();

    @Builder
    public Team(String name, String description,String color, String startDate, String endDate, String figma, String github, String discord) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.startDate = stringToLocalDate(startDate);
        this.endDate = stringToLocalDate(endDate);
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

    public void addSchedule(Schedule schedule) {
        schedule.setTeam(this);
        this.schedules.add(schedule);
    }

    public void update(UpdateTeam updateTeam) {
        this.name = updateTeam.getName();
        this.description = updateTeam.getDescription();
        this.color = updateTeam.getColor();
        this.startDate = stringToLocalDate(updateTeam.getStartDate());
        this.endDate = stringToLocalDate(updateTeam.getEndDate());
        this.figma = updateTeam.getFigma();
        this.github = updateTeam.getGithub();
        this.discord = updateTeam.getDiscord();
    }

    private LocalDate stringToLocalDate(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateTime, formatter);
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}