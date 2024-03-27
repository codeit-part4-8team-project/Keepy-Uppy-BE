package com.keepyuppy.KeepyUppy.schedule.communication.response;

import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamWithoutMemberResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Schema(name = "팀 정보를 포함한 각 스케쥴 응답")
@Data
public class TeamScheduleWithTeamResponse extends ScheduleResponse {
    private MemberResponse author;
    private TeamWithoutMemberResponse teamResponse;

    public TeamScheduleWithTeamResponse(Member member, Team team, Long id, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.author = MemberResponse.of(member);
        this.teamResponse = TeamWithoutMemberResponse.of(team);
        setId(id);
        setTitle(title);
        setContent(content);
        setStartDateTime(startDateTime);
        setEndDateTime(endDateTime);
    }

    public static TeamScheduleWithTeamResponse of(Schedule schedule) {
        return new TeamScheduleWithTeamResponse(
                schedule.getMember(),
                schedule.getTeam(),
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }
}
