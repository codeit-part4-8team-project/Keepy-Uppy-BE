package com.keepyuppy.KeepyUppy.issue.communication.response;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamInContentResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "팀 내 이슈 진행상태별 리스트 응답")
@AllArgsConstructor
public class TeamIssueBoardResponse {
    private List<TeamIssueResponse> todoIssues;
    private List<TeamIssueResponse> progressIssues;
    private List<TeamIssueResponse> doneIssues;
    private TeamInContentResponse team;

    public static TeamIssueBoardResponse of(
            List<Issue> todoIssues,
            List<Issue> progressIssues,
            List<Issue> doneIssues,
            Team team
    ){
        return new TeamIssueBoardResponse(
                getResponses(todoIssues),
                getResponses(progressIssues),
                getResponses(doneIssues),
                TeamInContentResponse.of(team)
        );
    }

    private static List<TeamIssueResponse> getResponses(List<Issue> issues){
        return issues.stream().map(TeamIssueResponse::of).toList();
    }
}
