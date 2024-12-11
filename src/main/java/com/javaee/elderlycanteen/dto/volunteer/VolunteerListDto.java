package com.javaee.elderlycanteen.dto.volunteer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VolunteerListDto {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private List<ResponseData> response;

    @Data
    public static class ResponseData {

        @JsonProperty("accountId")
        private String accountId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("idCard")
        private String idCard;

        @JsonProperty("phoneNum")
        private String phoneNum;

        @JsonProperty("deliverCount")
        private Integer deliverCount;

        @JsonProperty("score")
        private Double score;
    }
}
