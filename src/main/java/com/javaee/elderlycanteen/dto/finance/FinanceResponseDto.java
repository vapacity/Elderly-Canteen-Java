package com.javaee.elderlycanteen.dto.finance;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FinanceResponseDto {
    @JsonProperty("response")
    public List<FinanceResponseData> response;

    @JsonProperty("success")
    public String success;

    @JsonProperty("msg")
    public String msg;

    @Data
    public static class FinanceResponseData {
        @JsonProperty("financeId")
        public String financeId;

        @JsonProperty("financeType")
        public String financeType;

        @JsonProperty("financeDate")
        public String financeDate;

        @JsonProperty("price")
        public String price;

        @JsonProperty("inOrOut")
        public String inOrOut;

        @JsonProperty("accountId")
        public String accountId;

        @JsonProperty("administratorId")
        public String administratorId;

        @JsonProperty("proof")
        public Byte[] proof;

        @JsonProperty("status")
        public String status;


    }
}
