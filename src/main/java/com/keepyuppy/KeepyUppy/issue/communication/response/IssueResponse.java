package com.keepyuppy.KeepyUppy.issue.communication.response;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamInContentResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "팀 정보 포함한 이슈 정보 응답")
public class IssueResponse extends TeamIssueResponse {

    private TeamInContentResponse team;

    public IssueResponse(Issue issue) {
        super(issue);
        this.team = TeamInContentResponse.of(issue.getTeam());
    }

    public static IssueResponse of(Issue issue){
        return new IssueResponse(issue);
    }

}
