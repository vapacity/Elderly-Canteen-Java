package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RestockResponseDto {

    @JsonProperty("data")
    private RestockData data;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;

    @Data
    public static class RestockData {

        @JsonProperty("administratorId")
        private String administratorId;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("expiry")
        private Date expiry;

        @JsonProperty("financeId")
        private String financeId;

        @JsonProperty("grade")
        private Integer grade;

        @JsonProperty("ingredientId")
        private String ingredientId;

        @JsonProperty("ingredientName")
        private String ingredientName;

        @JsonProperty("price")
        private Double price;

        @JsonProperty("date")
        private Date date;
    }
}
