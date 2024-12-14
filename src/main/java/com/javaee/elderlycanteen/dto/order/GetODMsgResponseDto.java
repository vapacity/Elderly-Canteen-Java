package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetODMsgResponseDto {

    @JsonProperty("msg")
    private String msg;  // Required field (non-nullable)

    @JsonProperty("response")
    private VolunteerMsg response;  // Nullable

    @JsonProperty("success")
    private Boolean success;  // Nullable (use Boolean instead of boolean to allow null)

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VolunteerMsg {

        @JsonProperty("VolunteerId")
        private Integer volunteerId;  // Nullable

        @JsonProperty("VolunteerName")
        private String volunteerName;  // Nullable
    }
}



