package com.javaee.elderlycanteen.entity;

import lombok.Data;

@Data
public class TokenInfo {
    private String token;
    private Integer accountId;
    private String accountName;
    private String identity;
}
