package com.keepyuppy.KeepyUppy.schedule.communication.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateScheduleRequest {
    private String title;
    private String content;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
