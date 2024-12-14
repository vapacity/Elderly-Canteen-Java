package com.javaee.elderlycanteen.dto.category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CateResponseDto {
    @JsonProperty("cates")
    private CateDto cates;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("msg")
    private String message;
}

