package com.keepyuppy.KeepyUppy;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.global.exception.ExceptionType;
import com.keepyuppy.KeepyUppy.member.communication.request.AddMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.request.UpdateMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Role;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.member.repository.MemberJpaRepository;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.member.service.MemberService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberRepositoryImpl memberRepository;

    @Autowired
    TeamService teamService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("팀에 속한 멤버들을 조회한다")
    void getMembers() {
        //given
        createUserAndTeam();

        //when
        List<MemberResponse> members = memberService.getMembers(1L);

        //then
        Assertions.assertEquals(1,members.size());
    }

    @Test
    @DisplayName("매니저 이상의 권한이 없으면 팀에 멤버를 초대할수 없다")
    void addMemberFail() {
        //given
        //유저 10명생성 ,  Id 1 인 유저가 팀을 생성
        createUserAndTeam();

        //Id 2인 유저를 팀에 초대 Grade : TeamMember
        Users teamMember = userRepository.findById(2L).get();
        Team team = teamJpaRepository.findById(1L).get();
        Member member = new Member(teamMember, team, Grade.TEAM_MEMBER, Status.ACCEPTED);
        memberJpaRepository.save(member);
        team.getMembers().add(member);

        CustomUserDetails customUserDetails = new CustomUserDetails(teamMember);
        AddMemberRequest addMemberRequest = new AddMemberRequest("testerUsername3");

        //when
        //Id 2인 팀원 권한인 유저가 testerUsername3 유저를 팀에 초대.
        //then
        //Exception발생
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> memberService.addMember(customUserDetails, 1L, addMemberRequest));
        Assertions.assertEquals(ExceptionType.ACTION_ACCESS_DENIED,customException.getType());
    }

    @Test
    @DisplayName("매니저 이상의 권한이 있으면 팀에 멤버를 초대할수 있다")
    void addMemberSuccess() {
        //given
        //유저 10명생성 ,  Id 1 인 유저가 팀을 생성
        createUserAndTeam();

        //Id 2인 유저를 팀에 초대 Grade : Manager
        Users teamMember = userRepository.findById(2L).get();
        Team team = teamJpaRepository.findById(1L).get();
        Member member = new Member(teamMember, team, Grade.MANAGER, Status.ACCEPTED);
        memberJpaRepository.save(member);
        team.getMembers().add(member);

        CustomUserDetails customUserDetails = new CustomUserDetails(teamMember);
        AddMemberRequest addMemberRequest = new AddMemberRequest("testerUsername3");

        //when
        //Id 2인 팀원 권한인 유저가 testerUsername3 유저를 팀에 초대.
        boolean addMember = memberService.addMember(customUserDetails, 1L, addMemberRequest);

        //then
        //초대성공
        Assertions.assertTrue(addMember);
        Assertions.assertEquals(3,team.getMembers().size());
    }

    @Test
    @DisplayName("팀에 초대받은 멤버의 초대 승낙")
    void acceptInviteTeam() {
        //given
        createUserAndTeam();
        Users teamOwner = userRepository.findById(1L).get();
        CustomUserDetails teamOwnerCustomUserDetails = new CustomUserDetails(teamOwner);
        memberService.addMember(teamOwnerCustomUserDetails, 1L, new AddMemberRequest("testerUsername3"));

        Users invitedUser = userRepository.findById(4L).get();
        CustomUserDetails invitedUserCustomUserDetails = new CustomUserDetails(invitedUser);

        //when
        memberService.accept(invitedUserCustomUserDetails, 1L);
        Member member = memberRepository.findMemberInTeamByUserId(invitedUser.getId(), 1L).get();

        //then
        Assertions.assertEquals("testerUsername3",member.getUser().getUsername());
    }

    @Test
    @DisplayName("팀에 초대받은 멤버의 초대 거절")
    void rejectInviteTeam() {
        //given
        createUserAndTeam();

        Users teamOwner = userRepository.findById(1L).get();
        CustomUserDetails teamOwnerCustomUserDetails = new CustomUserDetails(teamOwner);
        memberService.addMember(teamOwnerCustomUserDetails, 1L, new AddMemberRequest("testerUsername3"));

        Users invitedUser = userRepository.findById(4L).get();
        CustomUserDetails invitedUserCustomUserDetails = new CustomUserDetails(invitedUser);

        //when
        memberService.reject(invitedUserCustomUserDetails, 1L);

        boolean present = memberRepository.findMemberInTeamByUserId(invitedUser.getId(), 1L).isPresent();

        //then
        Assertions.assertFalse(present);
    }

    @Test
    @DisplayName("멤버 업데이트")
    void updateMember() {
        //given
        createUserAndTeam();
        Member member = memberRepository.findMemberInTeamByUserId(1L, 1L).get();
        Users user = member.getUser();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //when
        memberService.updateMember(customUserDetails, member.getId(), new UpdateMemberRequest("매니저", "백엔드"));

        //then
        Assertions.assertEquals(Grade.MANAGER.getName(),member.getGrade().getName());
        Assertions.assertEquals(Role.BACKEND.getName(),member.getRole().getName());
    }

    @Test
    @DisplayName("검색어 username 을 포함하는 팀 멤버 조회")
    void findByUsernamePattern() {
        //given
        createUserAndTeam();
        Team team = teamJpaRepository.findById(1L).get();
        Member member = new Member(userRepository.findById(2L).get(), team, Grade.TEAM_MEMBER, Status.ACCEPTED);
        memberJpaRepository.save(member);
        team.getMembers().add(member);


        Users user = userRepository.findById(1L).get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //when
        List<MemberResponse> tester = memberService.findByUsernamePattern(customUserDetails, 1L, "tester");
        List<MemberResponse> findBy0 = memberService.findByUsernamePattern(customUserDetails, 1L, "0");

        //then
        Assertions.assertEquals(2, tester.size());
        Assertions.assertEquals(1,findBy0.size());
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
        CreateTeamRequest createTeamRequest = new CreateTeamRequest("A팀", "A팀 입니다.", "red", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1), null, null, null, null);

        teamService.createTeam(customUserDetails, createTeamRequest);
    }
}
