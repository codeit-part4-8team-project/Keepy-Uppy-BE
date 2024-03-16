package com.keepyuppy.KeepyUppy.schedule.communication.response;

import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserScheduleResponse extends ScheduleResponse{
    private String userName;

    public UserScheduleResponse(String userName, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.userName = userName;
        setTitle(title);
        setContent(content);
        setStartDateTime(startDateTime);
        setEndDateTime(endDateTime);
    }

    public static UserScheduleResponse of(Schedule schedule) {
        return new UserScheduleResponse(
                schedule.getUser().getUsername(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }
}
