package com.keepyuppy.KeepyUppy.content.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "스케줄 생성 요청")
public class CreateScheduleRequest extends CreatePostRequest {
    
    private String startTime;
    private String endTime;
}
