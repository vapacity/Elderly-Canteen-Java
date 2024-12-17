package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.config.AudioConfig;
import com.javaee.elderlycanteen.decoder.AudioDecoder;
import com.javaee.elderlycanteen.webSocket.WebSocketClient;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@Service
public class AudioService {

    private static final AudioConfig audioconfig = new AudioConfig();
    private static final String HOST_URL = "https://iat-api.xfyun.cn/v2/iat";  // 讯飞API的 WebSocket 连接地址
    private static final String APP_ID = "4c36336d";  // 从讯飞获取的 App ID
    private static final String API_SECRET = "NzhiMmY3MWMzMDM2NTViMDIyMzdjZDhl";  // 从讯飞获取的 API Secret
    private static final String API_KEY = "ff6bd4074eee8ad4104151864b656e5e";  // 从讯飞获取的 API Key

    private final WebSocketClient webSocketClient;
    private final AudioDecoder decoder;
    private final CartItemService cartItemService;
    public AudioService(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
        this.decoder = new AudioDecoder();
        this.webSocketClient = new WebSocketClient(decoder);

    }

    public String processAudioFile(MultipartFile audioFile,int cartId) throws Exception {
        // 将 MultipartFile 转换为字节数组
        byte[] audioBytes = audioFile.getBytes();

        // 连接 WebSocket 并发送音频数据
        String url = WebSocketClient.getAuthUrl(HOST_URL, API_KEY, API_SECRET);
        WebSocket webSocket = webSocketClient.connect(url);

        // 将音频数据通过 WebSocket 发送
        webSocketClient.sendAudioData(webSocket, audioBytes);

        TimeUnit.SECONDS.sleep(1);

        String result = decoder.getDecodedText();
        decoder.reset();
        cartItemService.addCartItemByAudio(result,cartId);
        // 获取并解析响应结果
        return result;
    }
}
