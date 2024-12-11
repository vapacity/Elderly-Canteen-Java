package com.javaee.elderlycanteen.dto.authentication;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticationResponseDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String message;
}
