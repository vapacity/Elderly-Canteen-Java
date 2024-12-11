package com.javaee.elderlycanteen.dto.employeeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmployeeInfoRequsetDto {
    @JsonProperty("employeeId")
    private String employeeId;

    @JsonProperty("employeeName")
    private String employeeName;

    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("address")
    private String address;

    @JsonProperty("employeePosition")
    private String employeePosition;

    @JsonProperty("salary")
    private Double salary;

    @JsonProperty("idCard")
    private String idCard;

    @JsonProperty("isPaidThisMonth")
    private Boolean isPaidThisMonth;
}
