package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.employeeInfo.EmployeeInfoRequsetDto;
import com.javaee.elderlycanteen.dto.employeeInfo.EmployeeInfoResponseDto;
import com.javaee.elderlycanteen.entity.Employee;
import com.javaee.elderlycanteen.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 在这里添加特定的API端点方法
    @GetMapping("/getInfo")
    public List<EmployeeInfoResponseDto> getAllEmployees () {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{employeeId}")
    public EmployeeInfoResponseDto getEmployeeById (Integer employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

//    @PostMapping("/add")
//    public EmployeeInfoResponseDto addEmployee (EmployeeInfoRequsetDto request) {
//        return employeeService.addEmployee(request);
//    }

}