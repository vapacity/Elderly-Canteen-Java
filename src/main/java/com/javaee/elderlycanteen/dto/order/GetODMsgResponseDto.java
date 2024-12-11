package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetODMsgResponseDto {

    @JsonProperty("msg")
    private String msg;  // Required field (non-nullable)

    @JsonProperty("response")
    private VolunteerMsg response;  // Nullable

    @JsonProperty("success")
    private Boolean success;  // Nullable (use Boolean instead of boolean to allow null)

    @Data
    public static class VolunteerMsg {

        @JsonProperty("VolunteerId")
        private String volunteerId;  // Nullable

        @JsonProperty("VolunteerName")
        private String volunteerName;  // Nullable
    }
}



