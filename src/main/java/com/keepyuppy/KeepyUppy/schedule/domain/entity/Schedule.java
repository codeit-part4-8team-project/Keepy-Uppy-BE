package com.keepyuppy.KeepyUppy.schedule.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.request.UpdateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.domain.enums.ScheduleType;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    ScheduleType scheduleType;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Member member;

    private String title;
    private String content;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Schedule(ScheduleType scheduleType, Users user, Team team, Member member, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.scheduleType = scheduleType;
        this.user = user;
        this.team = team;
        this.member = member;
        this.title = title;
        this.content = content;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void update(UpdateScheduleRequest updateScheduleRequest) {
        this.title = updateScheduleRequest.getTitle();
        this.content = updateScheduleRequest.getContent();
        this.startDateTime = updateScheduleRequest.getStartDateTime();
        this.endDateTime = updateScheduleRequest.getEndDateTime();
    }

    public static Schedule ofUser(CreateScheduleRequest createScheduleRequest, Users user) {
        return new Schedule(ScheduleType.USER, user, null, null, createScheduleRequest.getTitle(), createScheduleRequest.getContent(), createScheduleRequest.getStartDateTime(), createScheduleRequest.getEndDateTime());
    }

    public static Schedule ofTeam(CreateScheduleRequest createScheduleRequest, Users user, Team team,Member member) {
        return new Schedule(ScheduleType.TEAM, user, team, member, createScheduleRequest.getTitle(), createScheduleRequest.getContent(), createScheduleRequest.getStartDateTime(), createScheduleRequest.getEndDateTime());
    }

}
