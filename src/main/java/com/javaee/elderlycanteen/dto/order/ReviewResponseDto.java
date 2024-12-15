package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("response")
    private List<ReviewResponseData> response;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewResponseData {

        @JsonProperty("cStarts")
        private Double cStars;

        @JsonProperty("cReviewText")
        private String cReviewText;

        @JsonProperty("dStarts")
        private Optional<Double> dStars;

        @JsonProperty("dReviewText")
        private Optional<String> dReviewText;
    }
}



