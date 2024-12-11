package com.javaee.elderlycanteen.dto.employeeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeInfoResponseDto {
    @JsonProperty("response")
    public List<EmployeeInfoRequsetDto> response;

    @JsonProperty("success")
    public String success;

    @JsonProperty("msg")
    public String msg;
}
