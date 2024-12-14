package com.javaee.elderlycanteen.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClearCartResponseDto {
    @JsonProperty("msg")
    private String msg;

    @JsonProperty("success")
    private Boolean success;
}
