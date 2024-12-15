package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javaee.elderlycanteen.config.DateDeserializer;
import com.javaee.elderlycanteen.config.DateSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class WMRequestDto {
    @JsonProperty("date")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;

    @JsonProperty("dishId")
    private Integer dishId;

    @JsonProperty("day")
    private String day;
}