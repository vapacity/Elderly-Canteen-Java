package com.javaee.elderlycanteen.decoder;

import com.javaee.elderlycanteen.dto.audio.Text;

public class AudioDecoder {

    private StringBuilder decodedText;

    public AudioDecoder() {
        this.decodedText = new StringBuilder();
    }

    public void decode(Text text) {
        // 处理返回的中间识别结果
        if (text != null) {
            decodedText.append(text.getText());
        }
    }

    public String getDecodedText() {
        return decodedText.toString();
    }
}
