package com.javaee.elderlycanteen.dto.volunteer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VolunteerApplicationDto {
    @JsonProperty("selfStatement")
    private String selfStatement;
}


