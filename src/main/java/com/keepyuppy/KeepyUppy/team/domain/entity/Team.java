package com.keepyuppy.KeepyUppy.team.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeamLinks;
import com.keepyuppy.KeepyUppy.team.domain.enums.Type;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Enumerated(EnumType.STRING)
    private Type type;
    private String name;
    private String description;
    @OneToMany(mappedBy = "team",fetch = FetchType.LAZY)
    private Set<Member> members = new HashSet<>();
    private LocalDate startDate;
    private LocalDate endDate;
    // todo
    // @OneToMany(mappedBy = "group")
//    private List<Issue> issues = new ArrayList<>();
    @OneToMany(mappedBy = "team",fetch = FetchType.LAZY)
    private List<Schedule> schedules = new ArrayList<>();
    private String figma;
    private String github;
    private String discord;

    @Builder
    public Team(String type,String name, String description, String startDate, String endDate, String figma, String github, String discord) {
        this.type = Type.getInstance(type);
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

    public void removeMember(Member member) {
        member.setTeam(null);
        this.members.remove(member);
    }

// todo
//    public void addIssue(Issue issue) {
//        issue.setTeam(this);
//        this.issues.add(issue);
//    }

    public void addSchedules(Schedule schedule) {
        schedule.setOrganization(this);
        this.schedules.add(schedule);
    }

    public void updateLinks(UpdateTeamLinks updateTeamLinks) {
        this.figma = updateTeamLinks.getFigma();
        this.github = updateTeamLinks.getGithub();
        this.discord = updateTeamLinks.getDiscord();
    }

    private LocalDate stringToLocalDate(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateTime, formatter);
    }
}