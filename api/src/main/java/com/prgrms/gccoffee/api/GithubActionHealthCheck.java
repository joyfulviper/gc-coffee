package com.prgrms.gccoffee.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubActionHealthCheck {

    @GetMapping("/api/v1/health")
    public String check() {
        return "헬스체크2";
    }
}