package com.keepyuppy.KeepyUppy.schedule.communication.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class ScheduleResponse {
    private String title;
    private String content;
    private String author;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
