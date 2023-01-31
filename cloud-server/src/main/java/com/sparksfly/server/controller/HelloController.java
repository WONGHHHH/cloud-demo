package com.sparksfly.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangh
 * @desc 测试
 * @date 2023/1/31 16:56
 */
@RestController
@RequestMapping("/auth")
public class HelloController {

    @GetMapping("/hello")
    public String index() {
        return "hello world.";
    }
}

