package com.javaee.elderlycanteen.dto.donate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DonationListDto {
    @JsonProperty("response")
    public List<ResponceData> response;

    @Data
    public static class ResponceData {
        @JsonProperty("origin")
        public String origin;

        @JsonProperty("price")
        public Double price;

        @JsonProperty("financeDate")
        public String financeDate;


    }
}
