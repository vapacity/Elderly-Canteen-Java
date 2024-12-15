package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentityResponseDto {

    @JsonProperty("success")
    private Boolean success;  // Nullable Boolean to represent success (can be null)

    @JsonProperty("msg")
    private String msg;  // Nullable String to represent the message (can be null)

    @JsonProperty("response")
    private IdentityDto response;  // Nullable IdentityDto (can be null)

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdentityDto {

        @JsonProperty("isDeliver")
        private Boolean isDeliver;  // Nullable Boolean for deliver status

        @JsonProperty("isOwner")
        private Boolean isOwner;  // Nullable Boolean for owner status
    }
}

