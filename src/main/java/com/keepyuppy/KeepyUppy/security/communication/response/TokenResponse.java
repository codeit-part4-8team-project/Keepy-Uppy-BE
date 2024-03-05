package com.keepyuppy.KeepyUppy.security.communication.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class TokenResponse {

    private String accessToken;
    private String refreshToken;

}
