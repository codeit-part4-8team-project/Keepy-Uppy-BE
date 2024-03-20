package com.keepyuppy.KeepyUppy.user.service;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.global.exception.ExceptionType;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.user.communication.request.UpdateUserRequest;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

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

    @Transactional
    public void updateProfileImage(CustomUserDetails userDetails, MultipartFile multipartFile) {

        try {
            String imageUrl = s3Service.upload(multipartFile, "images");
            log.info("사진 업로드에 성공하였습니다.");
            Users byUserName = findById(userDetails.getUserId());
            byUserName.setImageUrl(imageUrl);
            log.info("회원 프로필 이미지 변경 성공.");
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("사진 업로드에 실패하였습니다.");
            throw new CustomException(ExceptionType.PICTURE_UPLOAD_FAIL);
        }
    }

    // methods that encapsulate methods in userRepository
    public Users findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));
    }

    public Users findByOauthId(String oauthId){
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));
    }

    public Users findByRefreshToken(String refreshToken){
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
}

