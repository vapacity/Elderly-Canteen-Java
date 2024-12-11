package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CartResponseDto {
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
