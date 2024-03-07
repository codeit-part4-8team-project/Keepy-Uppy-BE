package com.keepyuppy.KeepyUppy.user.service;

import com.keepyuppy.KeepyUppy.global.exception.NotFoundException;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void updateRefreshToken(Users user, String newToken){
        user.updateRefreshToken(newToken);
        userRepository.save(user);
    }

    // methods that encapsulate methods in userRepository
    public Users findByOauthId(String oauthId){
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new NotFoundException.UserNotFoundException("존재하지 않는 유저입니다."));
    }

    public Users findByRefreshToken(String refreshToken){
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException.UserNotFoundException("존재하지 않는 유저입니다."));
    }


    public void create() {
        for (int i = 0; i < 5; i++) {
            Users users = new Users("user" + i, "null", "null", "", Provider.GITHUB, "");
            userRepository.save(users);
        }
    }
}
