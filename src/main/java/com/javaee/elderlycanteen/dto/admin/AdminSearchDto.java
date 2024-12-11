package com.javaee.elderlycanteen.dto.admin;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminSearchDto {
    @Data
    public static class AdminSearchData{
        @JsonProperty("name")
        private String name;

        @JsonProperty("accountId")
        private String accountId;

        @JsonProperty("phoneNum")
        private String phoneNum;

        @JsonProperty("position")
        private String position;

        @JsonProperty("gender")
        private String gender;

    }
}
