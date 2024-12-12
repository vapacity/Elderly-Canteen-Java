package com.javaee.elderlycanteen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {
    private Integer donationId;
    private Integer accountId;
    private Integer financeId;
    private String origin;
}