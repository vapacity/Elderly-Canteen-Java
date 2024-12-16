package com.javaee.elderlycanteen.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javaee.elderlycanteen.serializer.DateDeserializer;
import com.javaee.elderlycanteen.serializer.DateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("phoneNum")
    private String phoneNum;

    @JsonProperty("position")
    private String position;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("idCard")
    private String idCard;

    @JsonProperty("birthDate")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date birthDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

}
