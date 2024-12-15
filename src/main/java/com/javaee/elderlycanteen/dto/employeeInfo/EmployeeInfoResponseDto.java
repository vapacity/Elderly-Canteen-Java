package com.javaee.elderlycanteen.dto.employeeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeInfoResponseDto {
    @JsonProperty("response")
    public  EmployeeInfoResponseDate response;

    @JsonProperty("success")
    public boolean success;

    @JsonProperty("msg")
    public String msg;

    @Data
    public static class EmployeeInfoResponseDate {
        @JsonProperty("employeeId")
        public Integer employeeId;

        @JsonProperty("employeeName")
        public String employeeName;

        @JsonProperty("employeePosition")
        public String employeePosition;

        @JsonProperty("phoneNum")
        public String phoneNum;

        @JsonProperty("Address")
        public String Address;

        @JsonProperty("salary")
        public Double salary;

        @JsonProperty("idCard")
        public String idCard;

        @JsonProperty("isPaidThisMonth")
        public Boolean isPaidThisMonth;
    }
}
