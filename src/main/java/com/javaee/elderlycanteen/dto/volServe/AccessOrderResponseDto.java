package com.javaee.elderlycanteen.dto.volServe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccessOrderResponseDto {

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("response")
    private List<Response> response;

    @JsonProperty("success")
    private Boolean success;

    @Data
    public static class Response {

        @JsonProperty("orderId")
        private String orderId;

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
        private String updatedTime;  // You can use String or LocalDateTime based on the format

        @JsonProperty("orderDishes")
        private List<OrderDish> orderDishes;

        @Data
        public static class OrderDish {

            @JsonProperty("dishName")
            private String dishName;

            @JsonProperty("picture")
            private String picture;

            @JsonProperty("price")
            private Double price;

            @JsonProperty("quantity")
            private Integer quantity;
        }
    }
}


