package com.javaee.elderlycanteen.dto.admin;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AdminResponseDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String message;

    @JsonProperty("response")
    private List<AdminResponseData> response;

    @Data
    public static class AdminResponseData {
        @JsonProperty("name")
        private String name;

        @JsonProperty("idCard")
        private String idCard;

        @JsonProperty("birthDate")
        private String birthDate;

        @JsonProperty("address")
        private String address;

        @JsonProperty("email")
        private String email;
    }
}

