package com.javaee.elderlycanteen.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {

    @JsonProperty("response")
    private CartResponse response;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("success")
    private Boolean success;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartResponse{
        @JsonProperty("cartId")
        private Integer cartId;

        @JsonProperty("createTime")
        private Date createdTime;

        @JsonProperty("updatedTime")
        private Date updatedTime;
    }
}
