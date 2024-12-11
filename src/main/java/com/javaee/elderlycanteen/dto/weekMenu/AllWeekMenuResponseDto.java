package com.javaee.elderlycanteen.dto.weekMenu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AllWeekMenuResponseDto {
    @JsonProperty("Mon")
    private List<WeekDish> mon;

    @JsonProperty("Tue")
    private List<WeekDish> tue;

    @JsonProperty("Wed")
    private List<WeekDish> wed;

    @JsonProperty("Thu")
    private List<WeekDish> thu;

    @JsonProperty("Fri")
    private List<WeekDish> fri;

    @JsonProperty("Sat")
    private List<WeekDish> sat;

    @JsonProperty("Sun")
    private List<WeekDish> sun;

    @Data
    public static class WeekDish {
        @JsonProperty("category")
        private String category;

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("originalPrice")
        private Double originalPrice;

        @JsonProperty("discountPrice")
        private Double discountPrice;
    }
}


