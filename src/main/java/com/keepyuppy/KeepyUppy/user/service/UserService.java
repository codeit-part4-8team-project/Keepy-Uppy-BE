package com.keepyuppy.KeepyUppy.user.service;

import com.keepyuppy.KeepyUppy.global.exception.NotFoundException;
import com.keepyuppy.KeepyUppy.user.communication.request.UpdateUserRequest;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    @Transactional
    public Users updateUser(Long id, UpdateUserRequest request){
        String username = request.getUsername();

        validateUsername(username);
        Users user = findById(id);
        user.update(request);
        return userRepository.save(user);
    }

    public void validateUsername(String username){
        if (username == null) {
            return;
        } else if (username.length() < 5 || username.length() > 30) {
        throw new IllegalArgumentException("유저네임은 5자 이상, 30자 이하여야 합니다.");
        } else if (!USERNAME_PATTERN.matcher(username).matches()){
            throw new IllegalArgumentException("알파벳, 숫자, 밑줄(_)만 사용할 수 있습니다.");
        } else if (existsByUsername(username)){
            throw new IllegalArgumentException("이미 사용중인 유저네임입니다.");
        }
    }

    @Transactional
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @Transactional
    // Also responsible for saving new users when called in OAuth successHandler
    public void updateRefreshToken(Users user, String newToken){
        user.updateRefreshToken(newToken);
        userRepository.save(user);
    }


    // methods that encapsulate methods in userRepository
    public Users findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException.UserNotFoundException("존재하지 않는 유저입니다."));
    }

    public Users findByOauthId(String oauthId){
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new NotFoundException.UserNotFoundException("존재하지 않는 유저입니다."));
    }

    public Users findByRefreshToken(String refreshToken){
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException.UserNotFoundException("존재하지 않는 유저입니다."));
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
}
