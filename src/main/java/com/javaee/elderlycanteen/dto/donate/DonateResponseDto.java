package com.javaee.elderlycanteen.dto.donate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DonateResponseDto {
    @JsonProperty("success")
    public String success;

    @JsonProperty("msg")
    public String msg;
}
