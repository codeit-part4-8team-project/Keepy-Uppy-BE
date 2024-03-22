package com.keepyuppy.KeepyUppy.issue.communication.response;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamWithoutMemberResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "팀 내 이슈 진행상태별 리스트 응답")
@AllArgsConstructor
public class TeamIssueBoardResponse {

    private TeamWithoutMemberResponse team;
    private List<TeamIssueResponse> todoIssues;
    private List<TeamIssueResponse> progressIssues;
    private List<TeamIssueResponse> doneIssues;


    public static TeamIssueBoardResponse of(
            Team team,
            List<Issue> todoIssues,
            List<Issue> progressIssues,
            List<Issue> doneIssues
    ){
        return new TeamIssueBoardResponse(
                TeamWithoutMemberResponse.of(team),
                getResponses(todoIssues),
                getResponses(progressIssues),
                getResponses(doneIssues)
        );
    }

    private static List<TeamIssueResponse> getResponses(List<Issue> issues){
        return issues.stream().map(TeamIssueResponse::of).toList();
    }
}
