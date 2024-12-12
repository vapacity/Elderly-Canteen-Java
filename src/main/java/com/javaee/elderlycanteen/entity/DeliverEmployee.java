package com.javaee.elderlycanteen.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliverEmployee {

    private Integer employeeId;
    private Integer volunteerId;
}