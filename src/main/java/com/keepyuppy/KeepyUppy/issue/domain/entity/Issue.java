package com.keepyuppy.KeepyUppy.issue.domain.entity;

import com.keepyuppy.KeepyUppy.issue.communication.request.IssueRequest;
import com.keepyuppy.KeepyUppy.issue.communication.request.IssueStatusRequest;
import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Issue extends Post {

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @OneToMany(mappedBy = "issue")
    private Set<IssueAssignment> issueAssignments = new HashSet<>();

    @Builder(builderMethodName = "issueBuilder")
    public Issue(Team team, String title, Member author, String content, ContentType type,
                 Set<IssueAssignment> issueAssignments, LocalDateTime dueDate, IssueStatus status) {
        super(team, title, author, content, type);
        this.issueAssignments = issueAssignments != null ? issueAssignments : Collections.emptySet();
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
