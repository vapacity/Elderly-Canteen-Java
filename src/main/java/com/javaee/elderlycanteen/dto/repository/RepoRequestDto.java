package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date newExpiry;

    @JsonProperty("oldExpiry")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date oldExpiry;
}
