package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
    public static class Restocks {

        @JsonProperty("administratorId")
        private String administratorId;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("date")
        private Date date;

        @JsonProperty("expirationTime")
        private Date expirationTime;

        @JsonProperty("financeId")
        private String financeId;

        @JsonProperty("ingredientId")
        private String ingredientId;

        @JsonProperty("ingredientName")
        private String ingredientName;

        @JsonProperty("price")
        private Double price;
    }
}


