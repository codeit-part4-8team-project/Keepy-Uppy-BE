package com.keepyuppy.KeepyUppy.schedule.communication.response;

import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamScheduleResponse extends ScheduleResponse{
    private String teamName;

    public TeamScheduleResponse(String teamName, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.teamName = teamName;
        setTitle(title);
        setContent(content);
        setStartDateTime(startDateTime);
        setEndDateTime(endDateTime);
    }

    public static TeamScheduleResponse of(Schedule schedule) {
        return new TeamScheduleResponse(
                schedule.getTeam().getName(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }
}
