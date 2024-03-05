package com.keepyuppy.KeepyUppy.team.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @OneToMany(mappedBy = "team",fetch = FetchType.LAZY)
    private Set<Member> members = new HashSet<>();
    private LocalDate startDate;
    private LocalDate endDate;
    // todo
    // @OneToMany(mappedBy = "team")
//    private List<Issue> issues = new ArrayList<>();
    @OneToMany(mappedBy = "team",fetch = FetchType.LAZY)
    private List<Schedule> schedules = new ArrayList<>();
    private String figma;
    private String github;
    private String discord;

    @Builder
    public Team(String name, String description, String startDate, String endDate, String figma, String github, String discord) {

        this.name = name;
        this.description = description;
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

// todo
//    public void addIssue(Issue issue) {
//        issue.setTeam(this);
//        this.issues.add(issue);
//    }

    public void addSchedules(Schedule schedule) {
        schedule.setTeam(this);
        this.schedules.add(schedule);
    }

    public void setFigma(String figma) {
        this.figma = figma;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }

    private LocalDate stringToLocalDate(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateTime, formatter);
    }
}