package com.javaee.elderlycanteen.dto.finance;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class FinanceTotalsResponseDto {
    @JsonProperty("response")
    private FinanceData response;

    @JsonProperty("success")
    public Boolean success;

    @JsonProperty("msg")
    public String msg;

    @Data
    @AllArgsConstructor
    public static class FinanceData {
        @JsonProperty("netIn")
        public Double netIn;

        @JsonProperty("totalIn")
        public Double totalIn;

        @JsonProperty("totalOut")
        public Double totalOut;
    }
}
