package com.keepyuppy.KeepyUppy.team.domain.entity;

import com.keepyuppy.KeepyUppy.content.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.content.domain.entity.Post;
import com.keepyuppy.KeepyUppy.content.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
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