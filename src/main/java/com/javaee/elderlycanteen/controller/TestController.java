package com.javaee.elderlycanteen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/testapi")
    public String testApi() {
        return "Hello, World!";
    }
}
