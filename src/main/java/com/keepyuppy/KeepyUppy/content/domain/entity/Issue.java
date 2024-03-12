package com.keepyuppy.KeepyUppy.content.domain.entity;

import com.keepyuppy.KeepyUppy.content.communication.request.IssueRequest;
import com.keepyuppy.KeepyUppy.content.communication.request.IssueStatusRequest;
import com.keepyuppy.KeepyUppy.content.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.content.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Issue extends Post {

    @OneToMany(mappedBy = "issue")
    private Set<IssueAssignment> issueAssignments = new HashSet<>();

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Builder(builderMethodName = "issueBuilder")
    public Issue(Team team, String title, Member author, String content, ContentType type,
                 Set<IssueAssignment> issueAssignments, LocalDateTime dueDate, IssueStatus status) {
        super(team, title, author, content, type);
        this.issueAssignments = issueAssignments;
        this.dueDate = dueDate;
        this.status = status;
    }

    public void update(IssueRequest request){
        super.update(PostRequest.ofIssue(request));
        if (request.getDueDate() != null) this.dueDate = request.getDueDate();
        if (request.getStatus() != null) this.status = request.getStatus();
    }

    public void updateStatus(IssueStatusRequest request){
        if (request.getStatus() != null) this.status = request.getStatus();
    }

    public void setAssignments(Set<IssueAssignment> assignments){
        this.issueAssignments = assignments;
    }

}
