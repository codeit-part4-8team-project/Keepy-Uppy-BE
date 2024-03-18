package com.keepyuppy.KeepyUppy;

import com.keepyuppy.KeepyUppy.security.jwt.JwtUtils;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public String login() {

        Users build = Users.builder()
                .name("test")
                .imageUrl(null)
                .username("tester")
                .oauthId("oauthId")
                .provider(Provider.GOOGLE)
                .bio(null).build();

        userRepository.save(build);

        return jwtUtils.generateAccessToken(build.getOauthId());
    }
}
