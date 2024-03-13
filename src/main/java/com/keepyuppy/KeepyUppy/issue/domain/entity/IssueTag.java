package com.keepyuppy.KeepyUppy.issue.domain.entity;

import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class IssueTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color = "#808080";

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToMany(mappedBy = "tags")
    private Set<Issue> issues = new HashSet<>();

    @Builder
    public IssueTag(String name, Team team, String color){
        this.name = name;
        this.team = team;
        this.color = color;
    }

    public void addIssue(Issue issue){
        this.issues.add(issue);
    }

    public void removeIssue(Issue issue){
        this.issues.remove(issue);
    }

    public void setColor(String color) {
        this.color = color;
    }

}
