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

    @ManyToMany
    @JoinTable(name = "issue_tag",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<IssueTag> tags = new HashSet<>();

    @Builder(builderMethodName = "issueBuilder")
    public Issue(Team team, String title, Member author, String content, ContentType type,
                 Set<IssueAssignment> issueAssignments, LocalDateTime dueDate,
                 IssueStatus status, Set<IssueTag> tags) {
        super(team, title, author, content, type);
        this.issueAssignments = issueAssignments;
        this.dueDate = dueDate;
        this.status = status;
        this.tags = tags;
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

    public void addTag(IssueTag tag) {
        tags.add(tag);
        tag.addIssue(this);
    }

    public void resetTag() {
        tags.forEach(tag -> tag.removeIssue(this));
        this.tags = new HashSet<>();
    }
}
