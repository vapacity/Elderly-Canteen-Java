package com.javaee.elderlycanteen.dto.employeeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeInfoResponseDto {
    @JsonProperty("response")
    public List<EmployeeResponseDate> response;

    @Data
    public static class EmployeeResponseDate {
        @JsonProperty("EmployeeId")
        public String employeeId;

        @JsonProperty("EmployeeName")
        public String employeeName;

        @JsonProperty("PhoneNum")
        public String phoneNum;

        @JsonProperty("Address")
        public String address;

        @JsonProperty("EmployeePosition")
        public String employeePosition;

        @JsonProperty("Salary")
        public String salary;

        @JsonProperty("IdCard")
        public String idCard;

        @JsonProperty("IsPaidThisMonth")
        public String isPaidThisMonth;
    }
}
