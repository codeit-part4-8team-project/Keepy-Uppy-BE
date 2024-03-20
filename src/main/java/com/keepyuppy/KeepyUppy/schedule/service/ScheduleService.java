package com.keepyuppy.KeepyUppy.schedule.service;

import com.keepyuppy.KeepyUppy.global.exception.AccessDeniedException;
import com.keepyuppy.KeepyUppy.global.exception.NotFoundException;
import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.request.UpdateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.response.ScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.communication.response.TeamScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.communication.response.UserScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final UserRepository userRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public UserScheduleResponse createUserSchedule(CustomUserDetails userDetails, CreateScheduleRequest createScheduleRequest) {
        Users user = userRepository.findById(userDetails.getUserId()).orElseThrow(NotFoundException.UserNotFoundException::new);

        Schedule schedule = Schedule.ofUser(createScheduleRequest, user);

        scheduleJpaRepository.save(schedule);

        return UserScheduleResponse.of(schedule);
    }

    @Transactional
    public TeamScheduleResponse createTeamSchedule(CustomUserDetails userDetails, Long teamId, CreateScheduleRequest createScheduleRequest) {
        Users user = userRepository.findById(userDetails.getUserId()).orElseThrow(NotFoundException.UserNotFoundException::new);
        Team team = teamJpaRepository.findById(teamId).orElseThrow(NotFoundException.TeamNotFoundException::new);
        Schedule schedule = Schedule.ofTeam(createScheduleRequest, user, team);

        scheduleJpaRepository.save(schedule);

        return TeamScheduleResponse.of(schedule);
    }

    public List<UserScheduleResponse> getUserSchedule(Long userId) {
        return scheduleRepository.findUserSchedulesById(userId).stream().map(UserScheduleResponse::of).toList();
    }

    public Schedule getScheduleById(Long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId)
                .orElseThrow(NotFoundException.ScheduleNotFoundException::new);
    }

    public List<TeamScheduleResponse> getTeamSchedules(Long teamId) {
        return scheduleRepository.findTeamSchedulesByTeamId(teamId).stream().map(TeamScheduleResponse::of).toList();
    }

    @Transactional
    public ScheduleResponse updateSchedule(CustomUserDetails userDetails, Long scheduleId, UpdateScheduleRequest updateScheduleRequest) {
        Schedule schedule = getScheduleById(scheduleId);

        if (canUpdate(userDetails,schedule)) {
            schedule.update(updateScheduleRequest);

            if (schedule.getTeam() == null) {
                return UserScheduleResponse.of(schedule);
            } else {
                return TeamScheduleResponse.of(schedule);
            }
        } else {
            throw new AccessDeniedException.ActionAccessDeniedException();
        }
    }

    @Transactional
    public void deleteSchedule(CustomUserDetails userDetails, Long scheduleId) {
        Schedule schedule = getScheduleById(scheduleId);

        if (canUpdate(userDetails, schedule)) {
            scheduleJpaRepository.delete(schedule);
        } else {
            throw new AccessDeniedException.ActionAccessDeniedException();
        }
    }

    private boolean canUpdate(CustomUserDetails userDetails, Schedule schedule) {
        return schedule.getUser().getId().equals(userDetails.getUserId());
    }
}
