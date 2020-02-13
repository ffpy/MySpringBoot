package com.ganguomob.dev.myspringboot;

import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "登录")
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestBody @Validated LoginRequest request) {
        return "success: " + request;
    }
}
