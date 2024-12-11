package com.javaee.elderlycanteen.dto.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RepoResponseDto {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("message")
    private String message;
}
