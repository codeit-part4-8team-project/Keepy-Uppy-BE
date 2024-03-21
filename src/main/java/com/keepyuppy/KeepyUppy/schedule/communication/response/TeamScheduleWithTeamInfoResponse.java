package com.keepyuppy.KeepyUppy.schedule.communication.response;

import com.keepyuppy.KeepyUppy.team.communication.response.TeamWithoutMemberResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "팀정보를 포함한 팀 스케쥴 응답")
public class TeamScheduleWithTeamInfoResponse {
    private TeamWithoutMemberResponse teamResponse;
    private List<TeamScheduleResponse> teamSchedules;

    private TeamScheduleWithTeamInfoResponse(List<TeamScheduleResponse> teamSchedules,Team team) {
        this.teamSchedules = teamSchedules;
        this.teamResponse = TeamWithoutMemberResponse.of(team);
    }

    public static TeamScheduleWithTeamInfoResponse of(Team team, List<TeamScheduleResponse> teamSchedules) {
        return new TeamScheduleWithTeamInfoResponse(teamSchedules, team);
    }
}
