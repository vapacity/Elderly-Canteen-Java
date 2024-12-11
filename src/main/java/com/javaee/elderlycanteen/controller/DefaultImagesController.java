package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.DefaultImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/defaultImages")
public class DefaultImagesController {

    private final DefaultImagesService defaultImagesService;

    @Autowired
    public DefaultImagesController(DefaultImagesService defaultImagesService) {
        this.defaultImagesService = defaultImagesService;
    }

    // 在这里添加特定的API端点方法
}