package com.javaee.elderlycanteen.dto.finance;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javaee.elderlycanteen.config.DateDeserializer;
import com.javaee.elderlycanteen.config.DateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinanceResponseDto {
    @JsonProperty("response")
    public List<FinanceResponseData> response;

    @JsonProperty("success")
    public Boolean success;

    @JsonProperty("msg")
    public String msg;

    @Data
    @AllArgsConstructor
    public static class FinanceResponseData {
        @JsonProperty("financeId")
        public Integer financeId;

        @JsonProperty("financeType")
        public String financeType;

        @JsonProperty("financeDate")
        @JsonSerialize(using = DateSerializer.class)
        @JsonDeserialize(using = DateDeserializer.class)
        public Date financeDate;

        @JsonProperty("price")
        public Double price;

        @JsonProperty("inOrOut")
        public String inOrOut;

        @JsonProperty("accountId")
        public Integer accountId;

        @JsonProperty("administratorId")
        public Integer administratorId;

        @JsonProperty("proof")
        public Byte[] proof;

        @JsonProperty("status")
        public String status;


    }
}
