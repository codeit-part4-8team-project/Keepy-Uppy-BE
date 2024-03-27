package com.keepyuppy.KeepyUppy;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.member.service.MemberService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.communication.request.ChangeTeamOwnerRequest;
import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
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
import java.util.List;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TeamTest {

    @Autowired
    TeamService teamService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamJpaRepository teamJpaRepository;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("팀 생성자는 팀멤버이자 Owner 가 되고 팀이 생성된다")
    void createTeam() {
        //given
        createUsers(10);
        Users teamOwner = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(teamOwner);
        CreateTeamRequest createTeamRequest = new CreateTeamRequest("A팀", "A팀 입니다.", "red", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), null, null, null, null);

        //when
        TeamResponse team = teamService.createTeam(customUserDetails, createTeamRequest);

        //then
        Assertions.assertEquals("A팀",team.getName());
        Assertions.assertEquals(teamOwner.getUsername(),team.getMembers().get(0).getUsername());
        Assertions.assertEquals(Grade.OWNER.name(),team.getMembers().get(0).getGrade());
    }

    @Test
    @DisplayName("팀 Id 로 팀 조회")
    void getTeamById() {
        //given
        createUserAndTeam();
        //when
        TeamResponse team = teamService.getTeam(1L);

        //then
        Assertions.assertEquals("A팀",team.getName());
    }


    @Test
    @DisplayName("팀 생성시 추가한 회원은 팀 초대 대기중인 멤버가 된다")
    void createTeamWithAddMember() {
        //given
        createUsers(10);
        Users teamOwner = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(teamOwner);
        CreateTeamRequest createTeamRequest = new CreateTeamRequest("A팀", "A팀 입니다.", "red", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), List.of("testerUsername5","testerUsername6","testerUsername7"), null, null, null);

        //when
        teamService.createTeam(customUserDetails, createTeamRequest);

        Team team = teamJpaRepository.findById(1L).get();
        List<Member> list = team.getMembers().stream().filter(member -> member.getStatus().equals(Status.PENDING))
                .toList();

        //then
        Assertions.assertEquals(4,team.getMembers().size());
        Assertions.assertEquals(3,list.size());
    }

    @Test
    @DisplayName("로그인한 유저의 소속 팀 조회")
    void getTeamByUser() {
        //given
        createUserAndTeam();
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //when
        List<TeamResponse> teamByUser = teamService.getTeamByUser(customUserDetails);
        TeamResponse teamResponse = teamByUser.get(0);

        //then
        Assertions.assertEquals("A팀", teamResponse.getName());
    }


    @ParameterizedTest
    @ValueSource(longs = {6,7,8})
    @DisplayName("팀 소유자가 아니면 팀을 삭제할수 없다")
    void removeTeamFail(Long userId) {
        //given
        createUserAndTeam();
        Users user = userRepository.findById(userId).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        memberService.accept(customUserDetails,1L);

        //when
        boolean removedTeam = teamService.removeTeam(customUserDetails, 1L);

        //then
        Assertions.assertFalse(removedTeam);
    }

    @Test
    @DisplayName(("팀 소유자 일경우 팀을 삭제할수 있다"))
    void removeTeamSuccess() {
        //given
        createUserAndTeam();
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //when
        boolean removedTeam = teamService.removeTeam(customUserDetails, 1L);

        //then
        Assertions.assertTrue(removedTeam);
    }


    @DisplayName("일반 팀원은 팀 정보를 변경할수 없다")
    @ParameterizedTest
    @ValueSource(longs = {2,3,4,5,6,7,8,9})
    void updateTeamFail(Long userId) {
        //given
        createUserAndTeam();
        Users user = userRepository.findById(userId).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        UpdateTeamRequest updateTeamRequest = new UpdateTeamRequest("B팀", "B팀입니다", "black", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 3), "figma", "github", "discord");

        //when
        boolean updatedTeam = teamService.updateTeam(customUserDetails, 1L, updateTeamRequest);

        //then
        Assertions.assertFalse(updatedTeam);
    }

    @Test
    @DisplayName("팀 소유자는 팀 정보를 변경할수 없다")
    void updateTeamSuccess() {
        //given
        createUserAndTeam();
        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        UpdateTeamRequest updateTeamRequest = new UpdateTeamRequest("B팀", "B팀입니다", "black", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 3), "figma", "github", "discord");

        //when
        boolean updatedTeam = teamService.updateTeam(customUserDetails, 1L, updateTeamRequest);

        //then
        Assertions.assertTrue(updatedTeam);
    }

    @ParameterizedTest
    @ValueSource(strings = {"testerUsername5","testerUsername6","testerUsername7"})
    @DisplayName("로그인한 유저를 초대한 팀 목록 조회")
    void getPendingInvites(String username) {
        //given
        createUserAndTeam();
        Users user = userRepository.findByUsername(username).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //when
        List<TeamResponse> pendingTeams = teamService.getPendingTeams(customUserDetails);

        //then
        Assertions.assertEquals("A팀",pendingTeams.get(0).getName());
    }

    @Test
    @DisplayName("팀 소유자 변경")
    void changeTeamOwner() {
        //given
        createUserAndTeam();
        Users teamOwner = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(teamOwner);

        //when
        ChangeTeamOwnerRequest changeTeamOwnerRequest = new ChangeTeamOwnerRequest("testerUsername5");
        teamService.changeTeamOwner(customUserDetails,1L,changeTeamOwnerRequest);
        Team team = teamJpaRepository.findById(1L).get();

        //then
        Assertions.assertEquals(2L,team.getOwnerId());
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



