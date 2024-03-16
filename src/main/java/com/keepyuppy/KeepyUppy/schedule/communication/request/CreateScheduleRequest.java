package com.keepyuppy.KeepyUppy.schedule.communication.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateScheduleRequest {
    private String type;
    private String title;
    private String content;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
