package com.javaee.elderlycanteen.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class UsersResponseDto {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private List<UsersResponseData> response;

    @Data
    public static class UsersResponseData {
        @JsonProperty("name")
        private String name;

        @JsonProperty("idCard")
        private String idCard;

        @JsonProperty("birthDate")
        private String birthDate;

        @JsonProperty("address")
        private String address;
    }
}

