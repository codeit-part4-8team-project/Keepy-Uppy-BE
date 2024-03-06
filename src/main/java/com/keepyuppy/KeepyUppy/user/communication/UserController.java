package com.keepyuppy.KeepyUppy.user.communication;

import com.keepyuppy.KeepyUppy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public String test() {
        return "Hello User";
    }

    @PostMapping("/user/create")
    public void createUser() {
        userService.create();
    }

}
