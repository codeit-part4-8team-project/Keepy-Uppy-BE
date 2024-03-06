package com.keepyuppy.KeepyUppy.user.communication.response;

import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "회원 정보")
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String imageUrl;
    private String username;
    private String oauthId;
    private Provider provider;
    private String bio;

    public static UserResponse of(Users user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getImageUrl(),
                user.getUsername(),
                user.getOauthId(),
                user.getProvider(),
                user.getBio()
        );
    }
}
