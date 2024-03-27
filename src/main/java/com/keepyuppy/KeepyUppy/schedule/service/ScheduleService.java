package com.keepyuppy.KeepyUppy.schedule.service;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.global.exception.ExceptionType;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.request.UpdateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.response.*;
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

import java.time.LocalDate;
import java.util.ArrayList;
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
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Schedule schedule = Schedule.ofTeam(createScheduleRequest, user, team, member);

        scheduleJpaRepository.save(schedule);
        return TeamScheduleResponse.of(schedule);
    }

    public ScheduleResponse getScheduleById(CustomUserDetails userDetails, Long scheduleId) {
        Schedule schedule = scheduleJpaRepository.findById(scheduleId).orElseThrow(() -> new CustomException(ExceptionType.SCHEDULE_NOT_FOUND));

        if (schedule.getScheduleType().equals(ScheduleType.TEAM) && schedule.getTeam() != null) {
            // return if user is a member of the team
            getMemberInTeam(userDetails.getUserId(), schedule.getTeam().getId());
            return TeamScheduleResponse.of(schedule);

        } else if (canUpdate(userDetails, schedule)){
            // return if the schedule belongs to the user
            return UserScheduleResponse.of(schedule);

        } else {
            throw new CustomException(ExceptionType.SCHEDULE_NOT_FOUND);
        }
    }
    
    public SchedulesList getWeeklyScheduleFilter(Long userId, boolean showUser, List<Long> teamIds, LocalDate localDate) {
        List<Schedule> userSchedules = showUser ? scheduleRepository.findUserSchedulesByIdInWeek(userId, localDate) : new ArrayList<>();
        List<Schedule> teamSchedules = teamIds == null || teamIds.isEmpty() ? new ArrayList<>() : scheduleRepository.findTeamSchedulesByTeamIdsInWeek(userId, teamIds, localDate);
        return SchedulesList.of(userSchedules, teamSchedules);
    }

    public SchedulesList getMonthlyScheduleFilter(Long userId, boolean showUser, List<Long> teamIds, LocalDate localDate) {
        List<Schedule> userSchedules = showUser ? scheduleRepository.findUserSchedulesByIdInMonth(userId, localDate) : new ArrayList<>();
        List<Schedule> teamSchedules = teamIds == null || teamIds.isEmpty() ? new ArrayList<>() : scheduleRepository.findTeamSchedulesByTeamIdsInMonth(userId, teamIds, localDate);
        return SchedulesList.of(userSchedules, teamSchedules);
    }

    public TeamSchedulesList getTeamSchedulesInWeek(Long teamId, LocalDate localDate) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new CustomException(ExceptionType.TEAM_NOT_FOUND));
        List<TeamScheduleResponse> teamScheduleResponses = scheduleRepository.findTeamSchedulesByTeamIdInWeek(teamId, localDate).stream().map(TeamScheduleResponse::of).toList();
        return TeamSchedulesList.of(team, teamScheduleResponses);
    }

    public TeamSchedulesList getTeamSchedulesInMonth(Long teamId, LocalDate localDate) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new CustomException(ExceptionType.TEAM_NOT_FOUND));
        List<TeamScheduleResponse> teamScheduleResponses = scheduleRepository.findTeamSchedulesByTeamIdInMonth(teamId, localDate).stream().map(TeamScheduleResponse::of).toList();
        return TeamSchedulesList.of(team, teamScheduleResponses);
    }

    @Transactional
    public ScheduleResponse updateSchedule(CustomUserDetails userDetails, Long scheduleId, UpdateScheduleRequest updateScheduleRequest) {
        Schedule schedule = scheduleJpaRepository.findById(scheduleId).orElseThrow(() -> new CustomException(ExceptionType.SCHEDULE_NOT_FOUND));

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
        Schedule schedule = scheduleJpaRepository.findById(scheduleId).orElseThrow(() -> new CustomException(ExceptionType.SCHEDULE_NOT_FOUND));

        if (canUpdate(userDetails, schedule)) {
            scheduleJpaRepository.delete(schedule);
        } else {
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
    }

    public Member getMemberInTeam(Long userId, Long teamId){
        return memberRepository.findMemberInTeamByUserId(userId, teamId)
                .orElseThrow(() -> new CustomException(ExceptionType.TEAM_ACCESS_DENIED));
    }

    private boolean canUpdate(CustomUserDetails userDetails, Schedule schedule) {
        return schedule.getUser().getId().equals(userDetails.getUserId());
    }
}
