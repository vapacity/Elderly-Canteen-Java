package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AllRepoResponseDto {

    @JsonProperty("ingredients")
    private List<RepoDto> ingredients;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private boolean success;

    @Data
    public static class RepoDto {

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("expiry")
        private String expiry;

        @JsonProperty("grade")
        private Byte grade;

        @JsonProperty("ingredientId")
        private String ingredientId;

        @JsonProperty("ingredientName")
        private String ingredientName;
    }
}
