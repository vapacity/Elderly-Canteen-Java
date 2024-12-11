package com.javaee.elderlycanteen.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserSearchDto {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private List<SearchData> response;

    @Data
    public static class SearchData {
        @JsonProperty("accountName")
        private String accountName;

        @JsonProperty("accountId")
        private String accountId;

        @JsonProperty("phoneNum")
        private String phoneNum;

        @JsonProperty("identity")
        private String identity;

        @JsonProperty("gender")
        private String gender;
    }
}
