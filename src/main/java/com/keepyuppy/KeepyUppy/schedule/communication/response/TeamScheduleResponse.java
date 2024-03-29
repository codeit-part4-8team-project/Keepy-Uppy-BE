package com.keepyuppy.KeepyUppy.schedule.communication.response;

import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Schema(name = "팀 스케쥴 응답")
@Data
public class TeamScheduleResponse extends ScheduleResponse {
    private MemberResponse author;

    public TeamScheduleResponse(Member member,Long id, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.author = MemberResponse.of(member);
        setId(id);
        setTitle(title);
        setContent(content);
        setStartDateTime(startDateTime);
        setEndDateTime(endDateTime);
    }

    public static TeamScheduleResponse of(Schedule schedule) {
        return new TeamScheduleResponse(
                schedule.getMember(),
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }
}
