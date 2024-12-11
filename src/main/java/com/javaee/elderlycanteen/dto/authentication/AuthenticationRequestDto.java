package com.javaee.elderlycanteen.dto.authentication;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticationRequestDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("idCard")
    private String idCard;
}
