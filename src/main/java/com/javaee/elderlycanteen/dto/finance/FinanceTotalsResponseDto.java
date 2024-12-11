package com.javaee.elderlycanteen.dto.finance;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinanceTotalsResponseDto {
    @JsonProperty("response")
    private List<FinanceData> response;

    @JsonProperty("success")
    public String success;

    @JsonProperty("msg")
    public String msg;

    @Data
    public static class FinanceData {
        @JsonProperty("netIn")
        public Double netIn;

        @JsonProperty("totalIn")
        public Double totalIn;

        @JsonProperty("totalOut")
        public Double totalOut;
    }
}
