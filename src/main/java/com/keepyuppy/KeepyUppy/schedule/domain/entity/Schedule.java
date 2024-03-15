package com.keepyuppy.KeepyUppy.schedule.domain.entity;

import com.keepyuppy.KeepyUppy.schedule.domain.enums.ScheduleType;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void setUser(Users user) {
        this.user = user;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
