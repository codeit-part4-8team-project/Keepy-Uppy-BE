package com.keepyuppy.KeepyUppy;


import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.resopnse.TeamResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TeamTest {

    @Autowired
    TeamService teamService;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Test
    void createTeam() {
        //given
        Users users = Users.builder()
                .name("user1")
                .imageUrl("none")
                .oauthId("test").build();

        CreateTeamRequest createTeamRequest = new CreateTeamRequest(
                "team1",
                "테스트 팀",
                "2024-01-01",
                "2024-03-01"
        );


        //when
        Long teamId = teamService.createTeam(users, createTeamRequest);

        //then
        Assertions.assertEquals(1L, teamId);
    }

    @Test
    void getTeam() {
        //given
        Users users = Users.builder()
                .name("user1")
                .imageUrl("none")
                .oauthId("test").build();

        CreateTeamRequest createTeamRequest = new CreateTeamRequest(
                "team1",
                "테스트 팀",
                "2024-01-01",
                "2024-03-01"
        );

        Long teamId = teamService.createTeam(users, createTeamRequest);

        //when
        Team teamById = teamJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException());

        //then
        Assertions.assertEquals("team1", teamById.getName());
        Assertions.assertEquals("테스트 팀" , teamById.getDescription());
    }

    @Test
    void linkTest() {
        //given
        Users users = Users.builder()
                .name("user1")
                .imageUrl("none")
                .oauthId("test").build();

        CreateTeamRequest createTeamRequest = new CreateTeamRequest(
                "team1",
                "테스트 팀",
                "2024-01-01",
                "2024-03-01"
        );

        Long teamId = teamService.createTeam(users, createTeamRequest);

        //when
        Team teamById = teamJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException());

        //then
        Assertions.assertEquals("https://www.figma.com/",teamById.getFigma());
        Assertions.assertEquals("https://github.com/",teamById.getGithub());
        Assertions.assertEquals("https://discord.com/",teamById.getDiscord());
    }

    @Test
    @Transactional
    void getTeamResponseById() {
        //given
        Users users = Users.builder()
                .name("user1")
                .imageUrl("none")
                .oauthId("test").build();

        CreateTeamRequest createTeamRequest = new CreateTeamRequest(
                "team1",
                "테스트 팀",
                "2024-01-01",
                "2024-03-01"
        );

        Long teamId = teamService.createTeam(users, createTeamRequest);

        //when
        TeamResponse teamById = teamService.getTeamById(teamId);

        //then
        Assertions.assertEquals(1L, teamById.getId());
        Assertions.assertEquals("team1", teamById.getName());
        Assertions.assertEquals("테스트 팀", teamById.getDescription());
    }
}
