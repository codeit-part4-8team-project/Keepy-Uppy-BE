package com.keepyuppy.KeepyUppy;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.user.communication.request.UpdateUserRequest;
import com.keepyuppy.KeepyUppy.user.communication.response.UserResponse;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import com.keepyuppy.KeepyUppy.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    private Users user;

    @BeforeEach
    public void setup(){
        user = Users.builder().name("name")
                .imageUrl(null)
                .username("username")
                .oauthId(null)
                .provider(Provider.GOOGLE)
                .bio("bio")
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("유저 정보 업데이트")
    void updateUser(){
        //given
        UpdateUserRequest request = new UpdateUserRequest("new name", "new-username", "new bio");

        //when
        UserResponse response = userService.updateUser(1L, request);
        Users updatedUser = userRepository.findById(1L).orElse(null);

        //then
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("new name", response.getName());
        Assertions.assertEquals("new-username", response.getUsername());
        Assertions.assertEquals("new bio", response.getBio());
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    @DisplayName("유저 이름만 업데이트")
    void updateUserWithPartial(){
        //given
        UpdateUserRequest request = new UpdateUserRequest("new name", null, null);

        //when
        UserResponse response = userService.updateUser(1L, request);
        Users updatedUser = userRepository.findById(1L).orElse(null);

        //then
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("new name", response.getName());
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    @DisplayName("잘못된 유저네임으로 유저 정보 업데이트")
    void updateUserWithInvalidUsernames(){
        //given
        UpdateUserRequest request1 = new UpdateUserRequest(null, "new username", null);
        UpdateUserRequest request2 = new UpdateUserRequest(null, "", null);
        UpdateUserRequest request3 = new UpdateUserRequest(null, "username", null);

        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, request1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, request2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, request3));
    }

    @Test
    @DisplayName("유저 삭제")
    void deleteUser(){
        //when
        userService.deleteUser(1L);

        //then
        Assertions.assertThrows(CustomException.class, () -> userService.findById(1L));
    }

    @Test
    @DisplayName("유저 토큰 업데이트")
    void updateRefreshToken(){
        //when
        userService.updateRefreshToken(user, "token");
        Users updatedUser = userRepository.findById(1L).orElse(null);

        //then
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("token", updatedUser.getRefreshToken());
    }

    @Test
    @DisplayName("새 유저 저장 및 토큰 업데이트")
    void updateRefreshTokenForNewUser(){
        //when
        Users newUser = Users.builder().name("name")
                .imageUrl(null)
                .username("username2")
                .oauthId(null)
                .provider(Provider.GOOGLE)
                .bio("bio")
                .build();
        userService.updateRefreshToken(newUser, "token");
        Users updatedUser = userRepository.findById(2L).orElse(null);

        //then
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("token", updatedUser.getRefreshToken());
    }

    @Test
    @DisplayName("존재하는 유저네임으로 검색")
    void findByUsername(){
        //when
        UserResponse response = userService.findByUsername("username");

        //then
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("name", response.getName());
    }

    @Test
    @DisplayName("존재하지 않는 유저네임으로 검색")
    void findByNonExistentUsername(){
        //when
        UserResponse response = userService.findByUsername("new-username");

        //then
        Assertions.assertNull(response);
    }




}
