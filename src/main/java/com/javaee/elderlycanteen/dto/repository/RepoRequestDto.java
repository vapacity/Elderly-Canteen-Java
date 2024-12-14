package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;


@Data
public class RepoRequestDto {

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("grade")
    private Integer grade;

    @JsonProperty("ingredientId")
    private Integer ingredientId;

    @JsonProperty("ingredientName")
    private String ingredientName;

    @JsonProperty("newExpiry")
    private Date newExpiry;

    @JsonProperty("oldExpiry")
    private Date oldExpiry;
}
