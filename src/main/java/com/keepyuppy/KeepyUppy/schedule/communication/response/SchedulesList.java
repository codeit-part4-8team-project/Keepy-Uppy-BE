package com.keepyuppy.KeepyUppy.schedule.communication.response;

import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "유저, 팀 스케줗 배열 응답")
public class SchedulesList {
    private List<UserScheduleResponse> userSchedulesResponse;
    private List<TeamScheduleWithTeamResponse> teamSchedulesResponse;

    private SchedulesList(List<UserScheduleResponse> userSchedulesResponse, List<TeamScheduleWithTeamResponse> teamSchedulesResponse) {
        this.userSchedulesResponse = userSchedulesResponse;
        this.teamSchedulesResponse = teamSchedulesResponse;
    }

    public static SchedulesList of(List<Schedule> userSchedules, List<Schedule> teamSchedules) {
        return new SchedulesList(
                userSchedules.stream().map(UserScheduleResponse::of).toList(),
                teamSchedules.stream().map(TeamScheduleWithTeamResponse::of).toList()
        );
    }
}
