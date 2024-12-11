package com.javaee.elderlycanteen.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AdminRequestDto {
    @JsonProperty("AccountId")
    private String accountId;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("PhoneNum")
    private String phoneNum;

    @JsonProperty("Position")
    private String position;

    @JsonProperty("Gender")
    private String gender;
}
