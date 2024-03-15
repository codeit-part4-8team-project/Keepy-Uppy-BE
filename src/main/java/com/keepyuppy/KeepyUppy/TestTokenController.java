package com.keepyuppy.KeepyUppy;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestTokenController {
    private final TestService testService;

    @PostMapping("/test/token")
    public String getToken() {
        return testService.login();
    }
}
