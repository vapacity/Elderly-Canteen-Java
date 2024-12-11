package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ReviewResponseDto {

    @JsonProperty("success")
    private boolean success;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("response")
    private List<ReviewResponseData> response;

    @Data
    public static class ReviewResponseData {

        @JsonProperty("cStarts")
        private Double cStars;

        @JsonProperty("cReviewText")
        private String cReviewText;

        @JsonProperty("dStarts")
        private Double dStars;

        @JsonProperty("dReviewText")
        private String dReviewText;
    }
}



