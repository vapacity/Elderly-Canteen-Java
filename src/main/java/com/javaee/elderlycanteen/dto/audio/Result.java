package com.javaee.elderlycanteen.dto.audio;

import lombok.Data;

@Data
public class Result {
    private Ws[] ws;

    public Text getText() {
        Text text = new Text();
        StringBuilder sb = new StringBuilder();
        for (Ws ws : this.ws) {
            sb.append(ws.cw[0].w);
        }
        text.setText(sb.toString());
        return text;
    }
}
