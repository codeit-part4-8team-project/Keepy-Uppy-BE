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

    public void create() {
        for (int i = 0; i < 5; i++) {
            Users users = new Users("user" + i, "null", "null", Provider.GITHUB);
            userRepository.save(users);
        }
    }
}
