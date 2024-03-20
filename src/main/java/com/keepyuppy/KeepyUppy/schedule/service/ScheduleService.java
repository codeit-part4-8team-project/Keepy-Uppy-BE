package com.keepyuppy.KeepyUppy.schedule.service;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.global.exception.ExceptionType;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.request.UpdateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.response.ScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.communication.response.TeamScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.communication.response.UserScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.schedule.domain.enums.ScheduleType;
import com.keepyuppy.KeepyUppy.schedule.repository.ScheduleJpaRepository;
import com.keepyuppy.KeepyUppy.schedule.repository.ScheduleRepository;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final UserRepository userRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepositoryImpl memberRepository;

    @Transactional
    public UserScheduleResponse createUserSchedule(CustomUserDetails userDetails, CreateScheduleRequest createScheduleRequest) {
        Users user = userRepository.findById(userDetails.getUserId()).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));

        Schedule schedule = Schedule.ofUser(createScheduleRequest, user);

        scheduleJpaRepository.save(schedule);

        return UserScheduleResponse.of(schedule);
    }

    @Transactional
    public TeamScheduleResponse createTeamSchedule(CustomUserDetails userDetails, Long teamId, CreateScheduleRequest createScheduleRequest) {
        Users user = userRepository.findById(userDetails.getUserId()).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new CustomException(ExceptionType.TEAM_NOT_FOUND));
        Member member = memberRepository.findMemberInTeamByUserId(user.getId(), team.getId()).orElseThrow(() -> new CustomException(ExceptionType.TEAM_ACCESS_DENIED));
        Schedule schedule = Schedule.ofTeam(createScheduleRequest, user, team, member);

        if (team.getMembers().contains(memberRepository.findMemberInTeamByUserId(user.getId(), teamId).orElseThrow(MemberException.MemberNotFoundException::new))) {
            scheduleJpaRepository.save(schedule);

            return TeamScheduleResponse.of(schedule);
        } else {
            throw new IllegalStateException();
        }
    }

    public List<UserScheduleResponse> getUserScheduleInWeek(Long userId, LocalDateTime localDateTime) {
        return scheduleRepository.findUserSchedulesByIdInWeek(userId, localDateTime).stream().map(UserScheduleResponse::of).toList();
    }

    public List<UserScheduleResponse> getUserScheduleInMonth(Long userId, LocalDateTime localDateTime) {
        return scheduleRepository.findUserSchedulesByIdInMonth(userId, localDateTime).stream().map(UserScheduleResponse::of).toList();
    }


    public ScheduleResponse getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleJpaRepository.findById(scheduleId).orElseThrow(() -> new CustomException(ExceptionType.SCHEDULE_NOT_FOUND));

        if (schedule.getScheduleType().equals(ScheduleType.TEAM)) {
            return TeamScheduleResponse.of(schedule);
        } else {
            return UserScheduleResponse.of(schedule);
        }
    }

    public List<TeamScheduleResponse> getTeamSchedulesInweek(Long teamId, LocalDateTime localDateTime) {
        return scheduleRepository.findTeamSchedulesByTeamIdInWeek(teamId, localDateTime).stream().map(TeamScheduleResponse::of).toList();
    }

    public List<TeamScheduleResponse> getTeamSchedulesInMonth(Long teamId, LocalDateTime localDateTime) {
        return scheduleRepository.findTeamSchedulesByTeamIdInMonth(teamId, localDateTime).stream().map(TeamScheduleResponse::of).toList();
    }

    @Transactional
    public ScheduleResponse updateSchedule(CustomUserDetails userDetails, Long scheduleId, UpdateScheduleRequest updateScheduleRequest) {
        Schedule schedule = getScheduleById(scheduleId);

        if (canUpdate(userDetails, schedule)) {
            schedule.update(updateScheduleRequest);

            if (schedule.getTeam() == null) {
                return UserScheduleResponse.of(schedule);
            } else {
                return TeamScheduleResponse.of(schedule);
            }
        } else {
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
    }

    @Transactional
    public void deleteSchedule(CustomUserDetails userDetails, Long scheduleId) {
        Schedule schedule = getScheduleById(scheduleId);

        if (canUpdate(userDetails, schedule)) {
            scheduleJpaRepository.delete(schedule);
        } else {
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
    }

    private boolean canUpdate(CustomUserDetails userDetails, Schedule schedule) {
        return schedule.getUser().getId().equals(userDetails.getUserId());
    }
}
