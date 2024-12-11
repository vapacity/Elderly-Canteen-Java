package com.javaee.elderlycanteen.dto.personInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PersonInfoResponseDto {

    @JsonProperty("alterSuccess")
    private Boolean alterSuccess;  // nullable Boolean

    @JsonProperty("getSuccess")
    private Boolean getSuccess;    // nullable Boolean, ensures nullability

    @JsonProperty("msg")
    private String msg;            // Required field

    @JsonProperty("response")
    private ResponseData response; // Required field

    @Data
    public static class ResponseData {

        // Required fields
        @JsonProperty("accountId")
        private String accountId;      // Account ID, required field

        @JsonProperty("accountName")
        private String accountName;    // Account name, required field

        @JsonProperty("phoneNum")
        private String phoneNum;       // Phone number, required field

        @JsonProperty("identity")
        private String identity;       // Identity type, required field

        @JsonProperty("portrait")
        private String portrait;       // Portrait URL, required field

        @JsonProperty("gender")
        private String gender;         // Gender, required field

        // Optional fields
        @JsonProperty("birthDate")
        private String birthDate;      // Optional field (nullable)

        @JsonProperty("address")
        private String address;        // Optional field (nullable)

        @JsonProperty("name")
        private String name;           // Optional field (nullable)

        @JsonProperty("idCard")
        private String idCard;         // Optional field (nullable)

        @JsonProperty("money")
        private Double money;      // Required field (money)

        @JsonProperty("subsidy")
        private Double subsidy;    // Optional field (nullable)
    }
}
