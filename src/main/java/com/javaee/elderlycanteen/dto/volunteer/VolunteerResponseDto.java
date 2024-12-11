package com.javaee.elderlycanteen.dto.volunteer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class VolunteerResponseDto {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private ResponseData response;

    @Data
    public static class ResponseData {

        @JsonProperty("accountId")
        private String accountId;

        @JsonProperty("deliverCount")
        private Integer deliverCount;

        @JsonProperty("time")
        private String time;

        @JsonProperty("name")
        private String name;
    }
}

