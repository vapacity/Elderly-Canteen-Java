package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
public class AllRestockResponseDto {

    @JsonProperty("message")
    private String message;

    @JsonProperty("restocks")
    private List<Restocks> restocks;

    @JsonProperty("success")
    private Boolean success;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Restocks {

        @JsonProperty("administratorId")
        private Integer administratorId;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("date")
        private Date date;

        @JsonProperty("expirationTime")
        private Date expirationTime;

        @JsonProperty("financeId")
        private Integer financeId;

        @JsonProperty("ingredientId")
        private Integer ingredientId;

        @JsonProperty("ingredientName")
        private String ingredientName;

        @JsonProperty("price")
        private Double price;
    }
}


