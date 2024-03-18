package com.keepyuppy.KeepyUppy.schedule.communication.response;

import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Schema(name = "팀 스케쥴 응답")
@Data
public class TeamScheduleResponse extends ScheduleResponse{
    private String teamName;

    public TeamScheduleResponse(String teamName,String author, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.teamName = teamName;
        setAuthor(author);
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
                schedule.getUser().getUsername(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }
}
