package com.keepyuppy.KeepyUppy.schedule.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(name = "스케쥴 수정 요청")
@Data
public class UpdateScheduleRequest {
    private String title;
    private String content;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
