package com.keepyuppy.KeepyUppy.team.domain.entity;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeam;
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
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Post> announcements = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Issue> issues = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Schedule> schedules = new ArrayList<>();



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

    public void addContent(Post post) {
        post.setTeam(this);
        ContentType type = post.getType();
        switch (type) {
            case POST -> this.posts.add(post);
            case ANNOUNCEMENT -> this.announcements.add(post);
            case ISSUE -> this.issues.add((Issue) post);
            case SCHEDULE -> this.schedules.add((Schedule) post);
        }
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