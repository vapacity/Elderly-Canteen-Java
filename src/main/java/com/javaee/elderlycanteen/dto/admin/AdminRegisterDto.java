package com.javaee.elderlycanteen.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private Date birthDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

}
