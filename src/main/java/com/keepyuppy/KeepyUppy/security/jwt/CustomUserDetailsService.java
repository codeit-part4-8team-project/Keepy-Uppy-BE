package com.keepyuppy.KeepyUppy.security.jwt;

import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.exception.UserException;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String oauthId) throws UsernameNotFoundException {
        Users user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserException.UserNotFoundException("회원을 찾을수 없습니다."));

        return new CustomUserDetails(user);
    }

}
