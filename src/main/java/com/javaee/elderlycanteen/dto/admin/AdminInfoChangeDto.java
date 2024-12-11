package com.javaee.elderlycanteen.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminInfoChangeDto {
    @JsonProperty("PhoneNum")
    private String phoneNum;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("BirthDate")
    private String birthDate;

    @JsonProperty("AccountName")
    private String accountName;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Address")
    private String address;
}
