package com.javaee.elderlycanteen.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminRegisterDto {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("PhoneNum")
    private String phoneNum;

    @JsonProperty("Position")
    private String position;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("IdCard")
    private String idCard;

    @JsonProperty("BirthDate")
    private String birthDate;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Email")
    private String email;

}
