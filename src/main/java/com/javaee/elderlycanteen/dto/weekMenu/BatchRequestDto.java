package com.javaee.elderlycanteen.dto.weekMenu;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class BatchRequestDto {

    @JsonProperty("date")
    private Date date;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("dishIds")
    private List<String> dishIds;
}
