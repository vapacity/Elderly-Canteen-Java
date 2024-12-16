package com.javaee.elderlycanteen.webSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.javaee.elderlycanteen.decoder.AudioDecoder;
import com.javaee.elderlycanteen.dto.audio.AudioResponseDto;
import com.javaee.elderlycanteen.dto.audio.Text;
import okhttp3.*;
import com.javaee.elderlycanteen.config.AudioConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class WebSocketClient extends WebSocketListener {
    private final AudioConfig audioConfig = new AudioConfig();
    private static final Gson gson = new Gson();
    private AudioDecoder decoder;
    private static final int FRAME_SIZE = 1280;  // 每一帧的大小
    private static final int SAMPLE_INTERVAL = 40;  // 每帧之间的时间间隔（ms）

    public WebSocketClient(AudioDecoder decoder) {
        this.decoder = decoder;
    }

    public WebSocket connect(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();
        WebSocket webSocket = client.newWebSocket(request, this);
        return webSocket;
    }

    public void sendAudioData(WebSocket webSocket, byte[] audioData) {
        // 将音频数据按帧发送
        int totalFrames = audioData.length / FRAME_SIZE;
        for (int i = 0; i < totalFrames; i++) {
            int offset = i * FRAME_SIZE;
            byte[] frameData = Arrays.copyOfRange(audioData, offset, offset + FRAME_SIZE);
            int status = (i == 0) ? 0 : (i == totalFrames - 1) ? 2 : 1;  // 根据帧的顺序设置状态

            JsonObject frame = createAudioFrame(frameData, status);
            webSocket.send(frame.toString());
            try {
                Thread.sleep(SAMPLE_INTERVAL);  // 模拟音频采样的延时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private JsonObject createAudioFrame(byte[] audioData, int status) {
        JsonObject frame = new JsonObject();
        JsonObject business = new JsonObject();
        JsonObject common = new JsonObject();
        JsonObject data = new JsonObject();

        common.addProperty("app_id", "4c36336d");

        // 填充业务信息（如语言、域名等）
        business.addProperty("language", "en_us");
        business.addProperty("domain", "iat");
        business.addProperty("accent", "mandarin");

        // 填充音频数据
        data.addProperty("status", status);  // 根据状态设置
        data.addProperty("audio", Base64.getEncoder().encodeToString(audioData));
        data.addProperty("format", "audio/L16;rate=16000");
        data.addProperty("encoding", "raw");

        frame.add("common", common);
        frame.add("business", business);
        frame.add("data", data);

        return frame;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);

        // 解析返回的结果
        AudioResponseDto resp = gson.fromJson(text, AudioResponseDto.class);
        if (resp != null) {
            if (resp.getCode() != 0) {
                System.out.println("错误码: " + resp.getCode() + " 错误信息: " + resp.getMessage());
                return;
            }
            if (resp.getData() != null) {
                if (resp.getData().getResult() != null) {
                    Text resultText = resp.getData().getResult().getText();
                    decoder.decode(resultText);
                }
                if (resp.getData().getStatus() == 2) {
                    // 结束处理
                    System.out.println("识别完成");
                }
            }
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        System.err.println("WebSocket连接失败：" + t.getMessage());
    }

    // 重写的 getAuthUrl 方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        // 解析 hostUrl，获取主机和路径
        URL url = new URL(hostUrl);

        // 设置日期格式为 GMT
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());

        // 构建请求的签名字符串
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n")
                .append("date: ").append(date).append("\n")
                .append("GET ").append(url.getPath()).append(" HTTP/1.1");

        // 使用 UTF-8 编码生成字符集
        Charset charset = Charset.forName("UTF-8");

        // 使用 HMAC-SHA256 加密生成签名
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "HmacSHA256");
        mac.init(spec);

        // 对请求字符串进行加密
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);

        // 构造 authorization 字符串
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                apiKey, "hmac-sha256", "host date request-line", sha);

        // 构建带有授权信息和时间戳的 WebSocket URL
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();

        // 返回最终的 WebSocket URL
        return httpUrl.toString();
    }
}
