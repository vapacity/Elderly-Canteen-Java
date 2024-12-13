package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestockResponseDto {

    @JsonProperty("data")
    private RestockData data;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestockData {

        @JsonProperty("administratorId")
        private Integer administratorId;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("expiry")
        private Date expiry;

        @JsonProperty("financeId")
        private Integer financeId;

        @JsonProperty("grade")
        private Integer grade;

        @JsonProperty("ingredientId")
        private Integer ingredientId;

        @JsonProperty("ingredientName")
        private String ingredientName;

        @JsonProperty("price")
        private Double price;

        @JsonProperty("date")
        private Date date;
    }
}
