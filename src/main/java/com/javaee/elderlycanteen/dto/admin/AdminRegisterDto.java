package com.javaee.elderlycanteen.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminRegisterDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("position")
    private String position;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("idCard")
    private String idCard;

    @JsonProperty("birthDate")
    private String birthDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

}
