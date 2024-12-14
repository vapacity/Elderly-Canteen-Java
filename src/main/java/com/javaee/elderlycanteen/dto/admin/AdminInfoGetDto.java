package com.javaee.elderlycanteen.dto.admin;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminInfoGetDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String message;

    @JsonProperty("response")
    private AdminInfoData response;
    @Data
    public static class AdminInfoData {
        @JsonProperty("accountId")
        private String accountId;

        @JsonProperty("accountName")
        private String accountName;

        @JsonProperty("phoneNum")
        private String phoneNum;

        @JsonProperty("portrait")
        private String portrait;

        @JsonProperty("gender")
        private String gender;

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

        @JsonProperty("money")
        private Double money;

        @JsonProperty("position")
        private String position;

    }
}
