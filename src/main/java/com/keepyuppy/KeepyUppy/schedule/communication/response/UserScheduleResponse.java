package com.keepyuppy.KeepyUppy.schedule.communication.response;

import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Schema(name = "유저 스케쥴 응답")
@Data
public class UserScheduleResponse extends ScheduleResponse{

    public UserScheduleResponse(String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        setTitle(title);
        setContent(content);
        setStartDateTime(startDateTime);
        setEndDateTime(endDateTime);
    }

    public static UserScheduleResponse of(Schedule schedule) {
        return new UserScheduleResponse(
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }
}
