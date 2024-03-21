package com.keepyuppy.KeepyUppy.issue.communication.response;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "유저별 이슈 진행상태별 리스트 응답")
@AllArgsConstructor
public class UserIssueBoardResponse {
    private List<IssueResponse> todoIssues;
    private List<IssueResponse> progressIssues;
    private List<IssueResponse> doneIssues;

    public static UserIssueBoardResponse of(
            List<Issue> todoIssues,
            List<Issue> progressIssues,
            List<Issue> doneIssues
    ){
        return new UserIssueBoardResponse(
                getResponses(todoIssues),
                getResponses(progressIssues),
                getResponses(doneIssues)
        );
    }

    private static List<IssueResponse> getResponses(List<Issue> issues){
        return issues.stream().map(IssueResponse::of).toList();
    }
}
