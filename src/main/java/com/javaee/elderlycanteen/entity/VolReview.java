package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolReview {
    private Integer applicationId;
    private Integer administratorId;
    private String status;
    private String reason;
    private Date reviewDate;
}