package com.javaee.elderlycanteen.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderItem {

    private Integer orderId;
    
    private String cusAddress;

    @JsonProperty("deliverOrDining")
    private Boolean deliverOrDining;

    @JsonProperty("deliverStatus")
    private String deliverStatus;

    @JsonProperty("money")
    private Double money;

    @JsonProperty("orderDishes")
    private List<OrderDish> orderDishes;

    @JsonProperty("remark")
    private String remark;

    @JsonProperty("status")
    private String status;

    @JsonProperty("subsidy")
    private Double subsidy;

    @JsonProperty("updatedTime")
    private Date updatedTime;
}
