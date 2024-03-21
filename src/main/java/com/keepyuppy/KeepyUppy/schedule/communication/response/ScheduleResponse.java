package com.keepyuppy.KeepyUppy.schedule.communication.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "스케쥴 응답")
public abstract class ScheduleResponse {
    private String title;
    private String content;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
