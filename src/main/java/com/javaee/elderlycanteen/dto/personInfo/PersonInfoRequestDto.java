package com.javaee.elderlycanteen.dto.personInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PersonInfoRequestDto {
    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birthDate")
    private Date birthDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("name")
    private String name;
}
