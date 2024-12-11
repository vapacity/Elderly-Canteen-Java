package com.javaee.elderlycanteen.dto.finance;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FinanceResponseDto {
    @JsonProperty("response")
    public List<FinanceResponseData> response;

    @Data
    public static class FinanceResponseData {
        @JsonProperty("FinanceId")
        public String financeId;

        @JsonProperty("FinanceType")
        public String financeType;

        @JsonProperty("FinanceDate")
        public String financeDate;

        @JsonProperty("Price")
        public String price;

        @JsonProperty("InOrOut")
        public String inOrOut;

        @JsonProperty("AccountId")
        public String accountId;

        @JsonProperty("AdministratorId")
        public String administratorId;

        @JsonProperty("Proof")
        public byte[] proof;

        @JsonProperty("Status")
        public String status;


    }
}
