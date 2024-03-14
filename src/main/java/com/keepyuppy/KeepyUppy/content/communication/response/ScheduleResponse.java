package com.keepyuppy.KeepyUppy.content.communication.response;

import com.keepyuppy.KeepyUppy.content.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "스케줄 정보 응답")
@AllArgsConstructor
public class ScheduleResponse {

    private Long id;
    private String title;
    private MemberResponse author;
    private String content;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public static ScheduleResponse of(Schedule schedule){
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                MemberResponse.of(schedule.getAuthor()),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }


}
