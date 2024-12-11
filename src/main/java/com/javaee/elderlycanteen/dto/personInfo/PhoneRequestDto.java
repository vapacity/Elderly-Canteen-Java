package com.javaee.elderlycanteen.dto.personInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PhoneRequestDto {
    @JsonProperty("newPhoneNum")
    private String newPhoneNum;
}
