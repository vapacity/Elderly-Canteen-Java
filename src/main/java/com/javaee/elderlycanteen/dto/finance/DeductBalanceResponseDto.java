package com.javaee.elderlycanteen.dto.finance;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class DeductBalanceResponseDto {
    @JsonProperty("financeId")
    public Integer financeId;

    @JsonProperty("bonus")
    public Optional<Double> bonus;

    @JsonProperty("success")
    public Boolean success;

    @JsonProperty("msg")
    public String msg;
}
