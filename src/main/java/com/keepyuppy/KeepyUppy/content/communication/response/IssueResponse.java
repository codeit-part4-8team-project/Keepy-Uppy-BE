package com.keepyuppy.KeepyUppy.content.communication.response;

import com.keepyuppy.KeepyUppy.content.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.content.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    public static IssueResponse of(Issue issue, Set<Member> assignedMembers){
        List<MemberResponse> memberResponses = null;
        if (!assignedMembers.isEmpty()) {
            memberResponses = assignedMembers.stream()
                    .filter(member -> member.getStatus().equals(Status.ACCEPTED))
                    .map(MemberResponse::new)
                    .toList();
        }

        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                new MemberResponse(issue.getAuthor()),
                issue.getContent(),
                memberResponses,
                issue.getDueDate(),
                issue.getStatus()
        );
    }


}
