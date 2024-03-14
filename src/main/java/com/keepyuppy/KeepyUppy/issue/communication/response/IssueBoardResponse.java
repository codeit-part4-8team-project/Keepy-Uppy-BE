package com.keepyuppy.KeepyUppy.issue.communication.response;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "이슈 진행상태별 리스트 응답")
@AllArgsConstructor
public class IssueBoardResponse {
    private List<IssueResponse> todoIssues;
    private List<IssueResponse> progressIssues;
    private List<IssueResponse> doneIssues;

    public static IssueBoardResponse of(
            List<Issue> todoIssues,
            List<Issue> progressIssues,
            List<Issue> doneIssues
    ){
        return new IssueBoardResponse(
                getResponses(todoIssues),
                getResponses(progressIssues),
                getResponses(doneIssues)
        );
    }

    private static List<IssueResponse> getResponses(List<Issue> issues){
        return issues.stream().map(IssueResponse::of).toList();
    }
}
