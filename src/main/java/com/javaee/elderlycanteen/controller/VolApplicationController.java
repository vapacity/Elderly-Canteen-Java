package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.volServe.AccessOrderResponseDto;
import com.javaee.elderlycanteen.service.VolApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/volApplications")
public class VolApplicationController {

    private final VolApplicationService volApplicationService;

    @Autowired
    public VolApplicationController(VolApplicationService volApplicationService) {
        this.volApplicationService = volApplicationService;
    }



}