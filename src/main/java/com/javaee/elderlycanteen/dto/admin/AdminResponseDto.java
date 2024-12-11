package com.javaee.elderlycanteen.dto.admin;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminResponseDto {
    @Data
    public static class AdminResponseData {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("IdCard")
        private String idCard;

        @JsonProperty("BirthDate")
        private String birthDate;

        @JsonProperty("Address")
        private String address;

        @JsonProperty("Email")
        private String email;
    }
}

