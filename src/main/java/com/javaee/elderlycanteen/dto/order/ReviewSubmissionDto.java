package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReviewSubmissionDto {
    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("cStars")
    private Double cStars;

    @JsonProperty("cReviewText")
    private String cReviewText;

    @JsonProperty("dStars")
    private Double dStars;

    @JsonProperty("dReviewText")
    private String dReviewText;
}
