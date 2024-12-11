package com.javaee.elderlycanteen.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsersRequestDto {

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("identity")
    private String identity;

    @JsonProperty("gender")
    private String gender;
}

