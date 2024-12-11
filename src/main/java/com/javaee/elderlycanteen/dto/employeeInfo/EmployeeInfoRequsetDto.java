package com.javaee.elderlycanteen.dto.employeeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmployeeInfoRequsetDto {
    @JsonProperty("EmployeeId")
    private String employeeId;

    @JsonProperty("EmployeeName")
    private String employeeName;

    @JsonProperty("PhoneNum")
    private String phoneNum;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("EmployeePosition")
    private String employeePosition;

    @JsonProperty("Salary")
    private Double salary;

    @JsonProperty("IdCard")
    private String idCard;

    @JsonProperty("IsPaidThisMonth")
    private Boolean isPaidThisMonth;
}
