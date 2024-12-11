package com.javaee.elderlycanteen.dto.volunteer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VolunteerReviewApplicationDto {
    @JsonProperty("reason")
    private String reason;

    @JsonProperty("status")
    private String status;
}
