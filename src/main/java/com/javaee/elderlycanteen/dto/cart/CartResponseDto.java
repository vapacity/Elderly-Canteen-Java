package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class CartResponseDto {

    @JsonProperty("response")
    private CartResponse response;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("success")
    private Boolean success;

    @Data
    public static class CartResponse{
        @JsonProperty("cartId")
        private String cartId;

        @JsonProperty("createTime")
        private Date createdTime;

        @JsonProperty("updatedTime")
        private Date updatedTime;
    }
}
