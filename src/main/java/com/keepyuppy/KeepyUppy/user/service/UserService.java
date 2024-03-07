package com.keepyuppy.KeepyUppy.user.service;

import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return userRepository.findByOauthId(oauthId).orElse(null);
    }

    public Users findByRefreshToken(String refreshToken){
        return userRepository.findByRefreshToken(refreshToken).orElse(null);
    }




    public void create() {
        for (int i = 0; i < 5; i++) {
            Users users = new Users("user" + i, "null", "null", "", Provider.GITHUB, "");
            userRepository.save(users);
        }
    }
}
