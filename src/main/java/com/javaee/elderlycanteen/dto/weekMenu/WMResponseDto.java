package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WMResponseDto {

    @JsonProperty("dish")
    private DishInfo dish;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;

    @Data
    @AllArgsConstructor
    public static class DishInfo {
        @JsonProperty("category")
        private String category;

        @JsonProperty("dishName")
        private String dishName;
    }
}


