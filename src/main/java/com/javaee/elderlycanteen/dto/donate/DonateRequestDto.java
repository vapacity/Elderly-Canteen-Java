package com.javaee.elderlycanteen.dto.donate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DonateRequestDto {
    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("price")
    private Double price;
}
