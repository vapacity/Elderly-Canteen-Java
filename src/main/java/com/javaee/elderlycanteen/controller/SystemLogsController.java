package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.entity.SystemLogs;
import com.javaee.elderlycanteen.service.SystemLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/systemLogs")
public class SystemLogsController {

    private final SystemLogsService systemLogsService;

    @Autowired
    public SystemLogsController(SystemLogsService systemLogsService) {
        this.systemLogsService = systemLogsService;
    }

}