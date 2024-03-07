package com.keepyuppy.KeepyUppy.security.communication.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "로그인한 회원 정보 및 토큰")
@AllArgsConstructor
public class LoginResponse {

    private String name;
    private String imageUrl;
    private String username;
    private String accessToken;
    private String refreshToken;

    private boolean newAccount;

    public static LoginResponse of(Users user, String accessToken, String refreshToken, boolean newAccount){
        return new LoginResponse(
                user.getName(),
                user.getImageUrl(),
                user.getUsername(),
                accessToken,
                refreshToken,
                newAccount);
    }

    @Override
    public String toString(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
