package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.EmployeeDao;
import com.javaee.elderlycanteen.dto.employeeInfo.EmployeeInfoRequsetDto;
import com.javaee.elderlycanteen.dto.employeeInfo.EmployeeInfoResponseDto;
import com.javaee.elderlycanteen.entity.Employee;
import com.javaee.elderlycanteen.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    // 在这里添加特定的业务逻辑方法
    public List<EmployeeInfoResponseDto> getAllEmployees(){
        List<EmployeeInfoResponseDto> result = new ArrayList<>();
        List<Employee> employees = employeeDao.getAllEmployees();
        if(employees == null){
            throw new NotFoundException("Employees not found");
        }
        for(Employee employee : employees){
            EmployeeInfoResponseDto response = new EmployeeInfoResponseDto();
            response.setResponse(new EmployeeInfoResponseDto.EmployeeInfoResponseDate());
            response.getResponse().setEmployeeId(employee.getEmployeeId());
            response.getResponse().setEmployeeName(employee.getEmployeeName());
            response.getResponse().setEmployeePosition(employee.getEmployeePosition());
            response.getResponse().setAddress(employee.getAddress());
            response.getResponse().setPhoneNum(employee.getPhoneNum());
            response.getResponse().setSalary(employee.getSalary());
            response.getResponse().setIdCard(employee.getIdCard());
            response.getResponse().setIsPaidThisMonth(employee.getIsPaidThisMonth());
            response.setSuccess(true);
            response.setMsg("get  employee successfully");
            result.add(response);
        }

        return result;
    }

    public EmployeeInfoResponseDto getEmployeeById (Integer employeeId){
        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            throw new NotFoundException("Employee not found");
        }
        EmployeeInfoResponseDto response = new EmployeeInfoResponseDto();
        response.setResponse(new EmployeeInfoResponseDto.EmployeeInfoResponseDate());
        response.getResponse().setEmployeeId(employee.getEmployeeId());
        response.getResponse().setEmployeeName(employee.getEmployeeName());
        response.getResponse().setEmployeePosition(employee.getEmployeePosition());
        response.getResponse().setAddress(employee.getAddress());
        response.getResponse().setPhoneNum(employee.getPhoneNum());
        response.getResponse().setSalary(employee.getSalary());
        response.getResponse().setIdCard(employee.getIdCard());
        response.getResponse().setIsPaidThisMonth(employee.getIsPaidThisMonth());
        response.setSuccess(true);
        response.setMsg("get  employee successfully , details are as follows");
        return response;
    }
}