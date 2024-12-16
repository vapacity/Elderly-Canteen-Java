package com.javaee.elderlycanteen.dto.audio;

import lombok.Data;

@Data
public class Text {
    private String text;

    public String getText() {
        return text;
    }
}
