package com.javaee.elderlycanteen.dto.volunteer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VolunteerReviewInfoDto {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private ResponseData response;

    @Data
    public static class ResponseData {

        @JsonProperty("phoneNum")
        private String phoneNum;

        @JsonProperty("gender")
        private String gender;

        @JsonProperty("birthDate")
        private String birthDate;

        @JsonProperty("name")
        private String name;

        @JsonProperty("idCard")
        private String idCard;

        @JsonProperty("selfStatement")
        private String selfStatement;
    }
}
