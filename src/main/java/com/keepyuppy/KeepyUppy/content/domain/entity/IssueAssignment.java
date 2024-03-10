package com.keepyuppy.KeepyUppy.content.domain.entity;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class IssueAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public IssueAssignment(Issue issue, Member member) {
        this.issue = issue;
        this.member = member;
    }
}
