package com.javaee.elderlycanteen.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClearCartResponseDto {
    @JsonProperty("msg")
    private String msg;

    @JsonProperty("success")
    private Boolean success;
}
