package com.keepyuppy.KeepyUppy.issue.communication.response;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
@Schema(name = "팀 정보 제외한 이슈 정보 응답")
public class TeamIssueResponse {

    private Long id;
    private String title;
    private MemberResponse author;
    private String content;
    private List<MemberResponse> assignedMembers;
    private LocalDate dueDate;
    private IssueStatus status;

    // default constructor for UserIssueResponse
    public TeamIssueResponse() {}

    public TeamIssueResponse(Issue issue) {
        this.id = issue.getId();
        this.title = issue.getTitle();
        this.author = MemberResponse.of(issue.getAuthor());
        this.content = issue.getContent();
        this.assignedMembers = getMemberResponses(issue);
        this.dueDate = issue.getDueDate();
        this.status = issue.getStatus();
    }

    public static TeamIssueResponse of(Issue issue){
        return new TeamIssueResponse(issue);
    }

    public static List<MemberResponse> getMemberResponses(Issue issue){
        if (issue.getIssueAssignments() == null) return Collections.emptyList();
        return issue.getIssueAssignments().stream()
                .map(assignment -> MemberResponse.of(assignment.getMember()))
                .toList();
    }


}
