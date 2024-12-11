package com.javaee.elderlycanteen.dto.volunteer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class VolunteerReviewDto {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private List<ResponseData> response;

    @Data
    public static class ResponseData {

        @JsonProperty("applicationId")
        private String applicationId;

        @JsonProperty("accountId")
        private String accountId;

        @JsonProperty("applicationDate")
        private String applicationDate;
    }
}

