package com.javaee.elderlycanteen.dto.finance;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinanceTotalsResponseDto {
    @JsonProperty("response")
    private List<FinanceData> response;

    @Data
    public static class FinanceData {
        @JsonProperty("NetIn")
        public Double netIn;

        @JsonProperty("TotalIn")
        public Double totalIn;

        @JsonProperty("TotalOut")
        public Double totalOut;
    }
}
