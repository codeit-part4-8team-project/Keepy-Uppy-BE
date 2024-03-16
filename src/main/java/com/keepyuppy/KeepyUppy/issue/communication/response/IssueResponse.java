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
@Schema(name = "이슈 정보 응답")
@AllArgsConstructor
public class IssueResponse {

    private Long id;
    private String title;
    private MemberResponse author;
    private String content;
    private List<MemberResponse> assignedMembers;
    private LocalDate dueDate;
    private IssueStatus status;
    private String teamName;
    private String teamColor;

    public static IssueResponse of(Issue issue){
        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                MemberResponse.of(issue.getAuthor()),
                issue.getContent(),
                getMemberResponses(issue),
                issue.getDueDate(),
                issue.getStatus(),
                issue.getTeam().getName(),
                issue.getTeam().getColor()
        );
    }

    public static List<MemberResponse> getMemberResponses(Issue issue){
        if (issue.getIssueAssignments() == null) return Collections.emptyList();
        return issue.getIssueAssignments().stream()
                .map(assignment -> MemberResponse.of(assignment.getMember()))
                .toList();
    }


}
