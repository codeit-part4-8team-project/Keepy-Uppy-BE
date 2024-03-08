package com.keepyuppy.KeepyUppy.content.domain.entity;

import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.content.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Issue extends Content {

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Member> assignedMembers;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Builder(builderMethodName = "issueBuilder")
    public Issue(Team team, String title, Member author, String content, ContentType type,
                 Set<Member> assignedMembers, LocalDateTime dueDate, IssueStatus status) {
        super(team, title, author, content, type);
        this.assignedMembers = assignedMembers;
        this.dueDate = dueDate;
        this.status = status;
    }

}
