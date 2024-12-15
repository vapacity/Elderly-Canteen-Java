package com.javaee.elderlycanteen.dto.volServe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import com.javaee.elderlycanteen.dto.order.OrderDish;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessOrderResponseDto {

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private List<Response> response;

    @JsonProperty("success")
    private Boolean success;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        @JsonProperty("orderId")
        private Integer orderId;

        @JsonProperty("cusAddress")
        private String cusAddress;

        @JsonProperty("deliverOrDining")
        private Boolean deliverOrDining;

        @JsonProperty("deliverStatus")
        private String deliverStatus;

        @JsonProperty("money")
        private Double money;

        @JsonProperty("status")
        private String status;

        @JsonProperty("remark")
        private String remark;

        @JsonProperty("subsidy")
        private Double subsidy;

        @JsonProperty("updatedTime")
        private Date updatedTime;  // You can use String or LocalDateTime based on the format

        @JsonProperty("orderDishes")
        private List<OrderDish> orderDishes;
    }
}


