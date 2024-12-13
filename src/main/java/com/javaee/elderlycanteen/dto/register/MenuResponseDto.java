package com.javaee.elderlycanteen.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MenuResponseDto {

    @JsonProperty("menu")
    private List<Menu> menu;

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private Boolean success;
}

