package com.javaee.elderlycanteen.dto.admin;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSearchDto {

        @JsonProperty("success")
        private boolean success;

        @JsonProperty("message")
        private String message;

        @JsonProperty("response")
        private List<AdminSearchData> response;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AdminSearchData{
                @JsonProperty("name")
                private String name;

                @JsonProperty("accountId")
                private Integer accountId;

                @JsonProperty("phoneNum")
                private String phoneNum;

                @JsonProperty("position")
                private String position;

                @JsonProperty("gender")
                private String gender;
        }
}
