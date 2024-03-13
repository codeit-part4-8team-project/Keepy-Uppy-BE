package com.keepyuppy.KeepyUppy.issue.communication.response;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
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
    private LocalDateTime dueDate;
    private IssueStatus status;
    private List<IssueTagResponse> tags;

    public static IssueResponse of(Issue issue){
        List<MemberResponse> memberResponses = issue.getIssueAssignments().stream()
                    .map(assignment -> new MemberResponse(assignment.getMember()))
                    .toList();

        List<IssueTagResponse> tagResponses = issue.getTags().stream()
                .map(tag -> IssueTagResponse.of(tag))
                .toList();

        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                new MemberResponse(issue.getAuthor()),
                issue.getContent(),
                memberResponses,
                issue.getDueDate(),
                issue.getStatus(),
                tagResponses
        );
    }


}
