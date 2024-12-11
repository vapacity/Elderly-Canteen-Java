package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WMRequestDto {
    @JsonProperty("date")
    private Date date;

    @JsonProperty("dishId")
    private String dishId;

    @JsonProperty("day")
    private String day;
}