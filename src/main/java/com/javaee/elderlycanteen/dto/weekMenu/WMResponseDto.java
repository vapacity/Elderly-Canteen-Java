package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WMResponseDto {

    @JsonProperty("dish")
    private DishInfo dish;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;

    @Data
    public static class DishInfo {
        @JsonProperty("category")
        private String category;

        @JsonProperty("dishName")
        private String dishName;
    }
}


