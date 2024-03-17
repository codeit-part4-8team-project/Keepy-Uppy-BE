package com.keepyuppy.KeepyUppy.schedule.repository;

import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.team.domain.entity.QTeam;
import com.keepyuppy.KeepyUppy.user.domain.entity.QUsers;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.keepyuppy.KeepyUppy.schedule.domain.entity.QSchedule.schedule;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Schedule> findUserSchedulesById(Long userId) {
        return jpaQueryFactory.selectFrom(schedule)
                .join(schedule.user, QUsers.users)
                .fetchJoin()
                .where(schedule.user.id.eq(userId))
                .where(schedule.team.isNull())
                .fetch();
    }

    public List<Schedule> findTeamSchedulesByTeamId(Long teamId) {
        return jpaQueryFactory.selectFrom(schedule)
                .join(schedule.team, QTeam.team)
                .fetchJoin()
                .where(schedule.team.id.eq(teamId))
                .fetch();
    }
}
