package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllRepoResponseDto {

    @JsonProperty("ingredients")
    private List<RepoDto> ingredients;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private boolean success;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RepoDto {

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("expiry")
        private Date expiry;

        @JsonProperty("grade")
        private Integer grade;

        @JsonProperty("ingredientId")
        private Integer ingredientId;

        @JsonProperty("ingredientName")
        private String ingredientName;
    }

}
