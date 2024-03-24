package com.keepyuppy.KeepyUppy;

import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.response.TeamScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.communication.response.UserScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.service.ScheduleService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ScheduleTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamService teamService;
    @Autowired
    ScheduleService scheduleService;

    @Test
    @DisplayName("유저 스케쥴 생성")
    void createUserSchedule() {
        //given
        createUserAndTeam();
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("USER", "스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 2, 12, 0));

        //when
        UserScheduleResponse userSchedule = scheduleService.createUserSchedule(customUserDetails, createScheduleRequest);

        //then
        Assertions.assertEquals(createScheduleRequest.getTitle(), userSchedule.getTitle());
        Assertions.assertEquals(createScheduleRequest.getContent(), userSchedule.getContent());
        Assertions.assertEquals(createScheduleRequest.getStartDateTime(), userSchedule.getStartDateTime());
        Assertions.assertEquals(createScheduleRequest.getEndDateTime(), userSchedule.getEndDateTime());
    }

    @Test
    @DisplayName("팀 스케쥴 생성")
    void createTeamSchedule() {
        //given
        createUserAndTeam();
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("USER", "스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 2, 12, 0));

        //when
        TeamScheduleResponse teamSchedule = scheduleService.createTeamSchedule(customUserDetails, 1L, createScheduleRequest);

        //then
        Assertions.assertEquals(createScheduleRequest.getTitle(), teamSchedule.getTitle());
        Assertions.assertEquals(createScheduleRequest.getContent(), teamSchedule.getContent());
        Assertions.assertEquals(createScheduleRequest.getStartDateTime(), teamSchedule.getStartDateTime());
        Assertions.assertEquals(createScheduleRequest.getEndDateTime(), teamSchedule.getEndDateTime());
        Assertions.assertEquals("testerUsername0",teamSchedule.getAuthor().getUsername());

    }

    @ParameterizedTest
    @ValueSource(ints = {18,19,20,21,22,23})
    @DisplayName("입력받은 날짜의 주에 속한 유저 스케쥴 조회")
    void getUserScheduleInWeek(int endDate) {
        //given
        createUserAndTeam();
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("USER", "스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 3, endDate, 12, 0));
        scheduleService.createUserSchedule(customUserDetails, createScheduleRequest);

        //when
        List<UserScheduleResponse> userScheduleInWeek = scheduleService.getUserScheduleInWeek(user.getId(), LocalDate.of(2024, 3, 20));

        //then
        Assertions.assertEquals(1,userScheduleInWeek.size());
    }

    void createUsers(int count) {
        for (int i = 0; i < count; i++) {
            Users user = Users.builder().name("testerName" + i)
                    .imageUrl(null)
                    .username("testerUsername" + i)
                    .oauthId(null)
                    .provider(Provider.GITHUB)
                    .bio("테스트 유저 입니다.")
                    .build();
            userRepository.save(user);
        }

    }


    void createUserAndTeam() {
        createUsers(10);
        Users teamOwner = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(teamOwner);
        CreateTeamRequest createTeamRequest = new CreateTeamRequest("A팀", "A팀 입니다.", "red", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), List.of("testerUsername5","testerUsername6","testerUsername7"), null, null, null);

        teamService.createTeam(customUserDetails, createTeamRequest);
    }
}
