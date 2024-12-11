package com.javaee.elderlycanteen.dto.dish;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DishResponseDto {
    @JsonProperty("dish")
    public DishDto dish;

    @JsonProperty("success")
    public String success;

    @JsonProperty("msg")
    public String msg;
}
