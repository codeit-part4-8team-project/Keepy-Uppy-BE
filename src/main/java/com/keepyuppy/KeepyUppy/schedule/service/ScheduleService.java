package com.keepyuppy.KeepyUppy.schedule.service;

import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.schedule.repository.ScheduleJpaRepository;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final UserRepository userRepository;
    private final TeamJpaRepository teamJpaRepository;

    @Transactional
    public void createUserSchedule(CustomUserDetails userDetails, CreateScheduleRequest createScheduleRequest) {
        Users user = userRepository.findById(userDetails.getUserId()).orElseThrow(IllegalArgumentException::new);

        Schedule schedule = Schedule.ofUser(createScheduleRequest, user);
        user.addSchedule(schedule);

        scheduleJpaRepository.save(schedule);
    }

    @Transactional
    public void createTeamSchedule(CustomUserDetails userDetails, Long teamId, CreateScheduleRequest createScheduleRequest) {
        Users user = userRepository.findById(userDetails.getUserId()).orElseThrow(IllegalArgumentException::new);
        Team team = teamJpaRepository.findById(teamId).orElseThrow(IllegalArgumentException::new);
        Schedule schedule = Schedule.ofTeam(createScheduleRequest, user, team);

        team.addSchedule(schedule);

        scheduleJpaRepository.save(schedule);
    }

    public void getUserSchedule(CustomUserDetails userDetails) {

    }
}
