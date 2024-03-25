package com.keepyuppy.KeepyUppy.schedule.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "스케쥴 수정 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleRequest {
    private String title;
    private String content;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
