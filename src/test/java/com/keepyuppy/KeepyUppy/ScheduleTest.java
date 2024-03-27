package com.keepyuppy.KeepyUppy;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.request.UpdateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.response.*;
import com.keepyuppy.KeepyUppy.schedule.service.ScheduleService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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

    @BeforeEach
    void setUp() {
        createUserAndTeam();
    }

    @Test
    @DisplayName("유저 스케쥴 생성")
    void createUserSchedule() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 2, 12, 0));

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
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 2, 12, 0));

        //when
        TeamScheduleResponse teamSchedule = scheduleService.createTeamSchedule(customUserDetails, 1L, createScheduleRequest);

        //then
        Assertions.assertEquals(createScheduleRequest.getTitle(), teamSchedule.getTitle());
        Assertions.assertEquals(createScheduleRequest.getContent(), teamSchedule.getContent());
        Assertions.assertEquals(createScheduleRequest.getStartDateTime(), teamSchedule.getStartDateTime());
        Assertions.assertEquals(createScheduleRequest.getEndDateTime(), teamSchedule.getEndDateTime());
        Assertions.assertEquals("testerUsername0",teamSchedule.getAuthor().getUsername());

    }

    @Test
    @DisplayName("입력받은 날짜의 주에 속한 유저 스케쥴 조회")
    void getUserScheduleInWeek() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        for (int i = 0; i < 5; i++) {
            CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 3, 3, 11, 0), LocalDateTime.of(2024, 3, 3+i, 12, 0));
            scheduleService.createUserSchedule(customUserDetails, createScheduleRequest);
        }

        //when
        SchedulesList scheduleResponse = scheduleService.getWeeklyScheduleFilter(user.getId(), true, null, LocalDate.of(2024, 3, 3));
        
        //then
        Assertions.assertEquals(5,scheduleResponse.getUserSchedulesResponse().size());
        Assertions.assertTrue(scheduleResponse.getTeamSchedulesResponse().isEmpty());
    }

    @Test
    @DisplayName("입력받은 날짜의 월에 속한 유저 스케쥴 조회")
    void getUserScheduleInMonth() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        for (int i = 0; i < 10; i++) {
            CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 3, 3, 11, 0), LocalDateTime.of(2024, 3, 3+i, 12, 0));
            scheduleService.createUserSchedule(customUserDetails, createScheduleRequest);
        }

        //when
        SchedulesList scheduleResponse = scheduleService.getMonthlyScheduleFilter(1L, true, null, LocalDate.of(2024, 3, 1));

        //then
        Assertions.assertEquals(10,scheduleResponse.getUserSchedulesResponse().size());
        Assertions.assertTrue(scheduleResponse.getTeamSchedulesResponse().isEmpty());
    }

    @Test
    @DisplayName("Id 로 스케쥴 단일 조회")
    void getScheduleById() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        for (int i = 0; i < 10; i++) {
            CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 3, 3, 11, 0), LocalDateTime.of(2024, 3, 3+i, 12, 0));
            scheduleService.createUserSchedule(customUserDetails, createScheduleRequest);
        }

        //when
        UserScheduleResponse scheduleById = (UserScheduleResponse)scheduleService.getScheduleById(customUserDetails, 1L);

        //then
        Assertions.assertEquals("스케쥴입니다", scheduleById.getTitle());
    }

    @Test
    @DisplayName("입력받은 날짜의 주에 속한 팀 스케쥴 조회")
    void getTeamScheduleInWeek() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        for (int i = 0; i < 3; i++) {
            CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("테스트코드작성", "테스트코드를작성해야한다", LocalDateTime.of(2024, 3, 3, 10, 10), LocalDateTime.of(2024, 3, 3+i, 12, 10));
            scheduleService.createTeamSchedule(customUserDetails, 1L, createScheduleRequest);
        }

        //when
        TeamSchedulesList teamSchedulesInWeek = scheduleService.getTeamSchedulesInWeek(1L, LocalDate.of(2024, 3, 3));
        List<TeamScheduleResponse> teamSchedules = teamSchedulesInWeek.getTeamSchedules();

        //then
        Assertions.assertEquals(3, teamSchedules.size());
    }

    @Test
    @DisplayName("입력받은 날짜의 월에 속한 팀 스케쥴 조회")
    void getTeamScheduleInMonth() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        for (int i = 0; i < 15; i++) {
            CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("테스트코드작성", "테스트코드를작성해야한다", LocalDateTime.of(2024, 3, 3, 10, 10), LocalDateTime.of(2024, 3, 3+i, 12, 10));
            scheduleService.createTeamSchedule(customUserDetails, 1L, createScheduleRequest);
        }

        //when
        TeamSchedulesList teamSchedulesInMonth = scheduleService.getTeamSchedulesInMonth(1L, LocalDate.of(2024, 3, 3));
        List<TeamScheduleResponse> teamSchedules = teamSchedulesInMonth.getTeamSchedules();

        //then
        Assertions.assertEquals(15, teamSchedules.size());
    }

    @Test
    @DisplayName("유저 스케쥴 수정")
    void updateUserSchedule() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("수정전", "수정하기 전입니다", LocalDateTime.of(2024, 3, 1, 11, 11), LocalDateTime.of(2024, 3, 3, 12, 12));
        scheduleService.createUserSchedule(customUserDetails, createScheduleRequest);

        //when
        UpdateScheduleRequest updateScheduleRequest = new UpdateScheduleRequest();
        ScheduleResponse scheduleResponse = scheduleService.updateSchedule(customUserDetails, 1L, updateScheduleRequest);

        //then
        Assertions.assertEquals(updateScheduleRequest.getTitle(), scheduleResponse.getTitle());
        Assertions.assertEquals(updateScheduleRequest.getContent(), scheduleResponse.getContent());
        Assertions.assertEquals(updateScheduleRequest.getStartDateTime(), scheduleResponse.getStartDateTime());
        Assertions.assertEquals(updateScheduleRequest.getEndDateTime(), scheduleResponse.getEndDateTime());
    }

    @Test
    @DisplayName("팀 스케쥴 수정")
    void updateTeamSchedule() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("수정전", "수정하기 전입니다", LocalDateTime.of(2024, 3, 1, 11, 11), LocalDateTime.of(2024, 3, 3, 12, 12));
        scheduleService.createTeamSchedule(customUserDetails, 1L, createScheduleRequest);

        //when
        UpdateScheduleRequest updateScheduleRequest = new UpdateScheduleRequest();
        ScheduleResponse scheduleResponse = scheduleService.updateSchedule(customUserDetails, 1L, updateScheduleRequest);

        //then
        Assertions.assertEquals(updateScheduleRequest.getTitle(), scheduleResponse.getTitle());
        Assertions.assertEquals(updateScheduleRequest.getContent(), scheduleResponse.getContent());
        Assertions.assertEquals(updateScheduleRequest.getStartDateTime(), scheduleResponse.getStartDateTime());
        Assertions.assertEquals(updateScheduleRequest.getEndDateTime(), scheduleResponse.getEndDateTime());
    }

    @Test
    @DisplayName("스케쥴 삭제")
    void deleteUserSchedule() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest("수정전", "수정하기 전입니다", LocalDateTime.of(2024, 3, 1, 11, 11), LocalDateTime.of(2024, 3, 3, 12, 12));
        scheduleService.createUserSchedule(customUserDetails, createScheduleRequest);

        //when
        scheduleService.deleteSchedule(customUserDetails, 1L);

        //then
        Assertions.assertThrows(CustomException.class, () -> scheduleService.getScheduleById(customUserDetails, 1L));

    }

    @Test
    @DisplayName("입력받은 날짜의 주에 속한 스케줄 필터링 및 조회")
    void getWeeklyScheduleFilter() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        for (int i = 0; i < 5; i++) {
            CreateScheduleRequest createUserScheduleRequest = new CreateScheduleRequest("스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 3, 3, 11, 0), LocalDateTime.of(2024, 3, 3+i, 12, 0));
            scheduleService.createUserSchedule(customUserDetails, createUserScheduleRequest);
            CreateScheduleRequest createTeamScheduleRequest = new CreateScheduleRequest("테스트코드작성", "테스트코드를작성해야한다", LocalDateTime.of(2024, 3, 3, 10, 10), LocalDateTime.of(2024, 3, 3+i, 12, 10));
            scheduleService.createTeamSchedule(customUserDetails, 1L, createTeamScheduleRequest);
        }

        //when
        SchedulesList scheduleResponse = scheduleService.getWeeklyScheduleFilter(user.getId(), true, List.of(1L), LocalDate.of(2024, 3, 3));

        //then
        Assertions.assertEquals(5,scheduleResponse.getUserSchedulesResponse().size());
        Assertions.assertEquals(5,scheduleResponse.getTeamSchedulesResponse().size());
    }

    @Test
    @DisplayName("입력받은 날짜의 월에 속한 스케쥴 필터링 및 조회")
    void getMonthlyScheduleFilter() {
        //given
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        for (int i = 0; i < 5; i++) {
            CreateScheduleRequest createUserScheduleRequest = new CreateScheduleRequest("스케쥴입니다", "스케쥴테스트중입니다", LocalDateTime.of(2024, 3, 3, 11, 0), LocalDateTime.of(2024, 3, 3+i, 12, 0));
            scheduleService.createUserSchedule(customUserDetails, createUserScheduleRequest);
            CreateScheduleRequest createTeamScheduleRequest = new CreateScheduleRequest("테스트코드작성", "테스트코드를작성해야한다", LocalDateTime.of(2024, 3, 3, 10, 10), LocalDateTime.of(2024, 3, 3+i, 12, 10));
            scheduleService.createTeamSchedule(customUserDetails, 1L, createTeamScheduleRequest);
        }

        //when
        SchedulesList scheduleResponse = scheduleService.getMonthlyScheduleFilter(1L, false, List.of(1L), LocalDate.of(2024, 3, 1));

        //then
        Assertions.assertEquals(0, scheduleResponse.getUserSchedulesResponse().size());
        Assertions.assertEquals(5,scheduleResponse.getTeamSchedulesResponse().size());
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
