package com.javaee.elderlycanteen.dto.admin;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String message;

    @JsonProperty("response")
    private AdminResponseData response;

    @Data
    public static class AdminResponseData {
        @JsonProperty("name")
        private String name;

        @JsonProperty("idCard")
        private String idCard;

        @JsonProperty("birthDate")
        private Date birthDate;

        @JsonProperty("address")
        private String address;

        @JsonProperty("email")
        private String email;
    }
}

