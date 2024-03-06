package com.keepyuppy.KeepyUppy;


import com.keepyuppy.KeepyUppy.organization.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.organization.communication.response.TeamResponse;
import com.keepyuppy.KeepyUppy.organization.domain.entity.Organization;
import com.keepyuppy.KeepyUppy.organization.repository.OrganizationJpaRepository;
import com.keepyuppy.KeepyUppy.organization.service.OrganizationService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrganizationTest {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    OrganizationJpaRepository organizationJpaRepository;

    @Test
    void createTeam() {
        //given
        Users users = Users.builder()
                .name("user1")
                .imageUrl("none")
                .oauthId("test").build();

        CreateTeamRequest createTeamRequest = new CreateTeamRequest(
                "team1",
                "백엔드",
                "테스트 팀",
                "2024-01-01",
                "2024-03-01"
        );


        //when
        Long teamId = organizationService.createTeam(users, createTeamRequest);

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
                "백엔드",
                "테스트 팀",
                "2024-01-01",
                "2024-03-01"
        );

        Long teamId = organizationService.createTeam(users, createTeamRequest);

        //when
        Organization teamById = organizationJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException());

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
                "백엔드",
                "테스트 팀",
                "2024-01-01",
                "2024-03-01"
        );

        Long teamId = organizationService.createTeam(users, createTeamRequest);

        //when
        Organization teamById = organizationJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException());

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
                "백엔드",
                "테스트 팀",
                "2024-01-01",
                "2024-03-01"
        );

        Long teamId = organizationService.createTeam(users, createTeamRequest);

        //when
        TeamResponse teamById = organizationService.getTeamById(teamId);

        //then
        Assertions.assertEquals(1L, teamById.getId());
        Assertions.assertEquals("team1", teamById.getName());
        Assertions.assertEquals("테스트 팀", teamById.getDescription());
    }
}
