package com.keepyuppy.KeepyUppy.schedule.repository;

import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.team.domain.entity.QTeam;
import com.keepyuppy.KeepyUppy.user.domain.entity.QUsers;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;

import static com.keepyuppy.KeepyUppy.schedule.domain.entity.QSchedule.schedule;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Schedule> findUserSchedulesByIdInWeek(Long userId, LocalDate date) {
        return jpaQueryFactory.selectFrom(schedule)
                .join(schedule.user, QUsers.users)
                .fetchJoin()
                .where(schedule.user.id.eq(userId))
                .where(schedule.team.isNull())
                .where(schedule.endDateTime.week().eq(date.get(WeekFields.ISO.weekOfWeekBasedYear())))
                .fetch();
    }

    public List<Schedule> findTeamSchedulesByTeamIdInWeek(Long teamId,LocalDate date) {
        return jpaQueryFactory.selectFrom(schedule)
                .join(schedule.team, QTeam.team)
                .fetchJoin()
                .where(schedule.team.id.eq(teamId))
                .where(schedule.endDateTime.week().eq(date.get(WeekFields.ISO.weekOfWeekBasedYear())))
                .fetch();
    }

    public List<Schedule> findUserSchedulesByIdInMonth(Long userId,LocalDate date) {
        return jpaQueryFactory.selectFrom(schedule)
                .join(schedule.user, QUsers.users)
                .fetchJoin()
                .where(schedule.user.id.eq(userId))
                .where(schedule.team.isNull())
                .where(schedule.endDateTime.month().eq(date.getMonthValue()))
                .fetch();
    }

    public List<Schedule> findTeamSchedulesByTeamIdInMonth(Long teamId,LocalDate date) {
        return jpaQueryFactory.selectFrom(schedule)
                .join(schedule.team, QTeam.team)
                .fetchJoin()
                .where(schedule.team.id.eq(teamId))
                .where(schedule.endDateTime.month().eq(date.getMonthValue()))
                .fetch();
    }


}
