package com.javaee.elderlycanteen.dto.authentication;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("idCard")
    private String idCard;
}
