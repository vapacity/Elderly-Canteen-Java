package com.javaee.elderlycanteen.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AdminRequestDto {
    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("position")
    private String position;

    @JsonProperty("gender")
    private String gender;
}
