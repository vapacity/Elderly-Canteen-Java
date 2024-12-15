package com.javaee.elderlycanteen.dto.authentication;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String message;
}
