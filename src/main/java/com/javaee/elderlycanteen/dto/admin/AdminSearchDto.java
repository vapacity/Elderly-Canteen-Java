package com.javaee.elderlycanteen.dto.admin;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSearchDto {

        @JsonProperty("name")
        private String name;

        @JsonProperty("accountId")
        private Integer accountId;

        @JsonProperty("phoneNum")
        private String phoneNum;

        @JsonProperty("position")
        private String position;

        @JsonProperty("gender")
        private String gender;


}
