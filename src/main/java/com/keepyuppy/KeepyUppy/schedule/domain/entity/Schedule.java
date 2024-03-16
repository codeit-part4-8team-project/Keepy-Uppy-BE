package com.keepyuppy.KeepyUppy.schedule.domain.entity;

import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.domain.enums.ScheduleType;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    ScheduleType scheduleType;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Team team;

    private String title;
    private String content;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Schedule(String type,Users user, Team team, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.scheduleType = ScheduleType.getInstance(type);
        this.user = user;
        this.team = team;
        this.title = title;
        this.content = content;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static Schedule ofUser(CreateScheduleRequest createScheduleRequest , Users user) {
        return new Schedule(createScheduleRequest.getType(), user, null, createScheduleRequest.getTitle(), createScheduleRequest.getContent(), createScheduleRequest.getStartDateTime(), createScheduleRequest.getEndDateTime());
    }

    public static Schedule ofTeam(CreateScheduleRequest createScheduleRequest,Users user, Team team) {
        return new Schedule(createScheduleRequest.getType(), user, team, createScheduleRequest.getTitle(), createScheduleRequest.getContent(), createScheduleRequest.getStartDateTime(), createScheduleRequest.getEndDateTime());
    }



    public void setUser(Users user) {
        this.user = user;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
