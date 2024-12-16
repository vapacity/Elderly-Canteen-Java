package com.javaee.elderlycanteen.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AudioConfig {

    @Value("${audio.hostUrl}")
    private String hostUrl;

    @Value("${audio.appid}")
    private String appid;

    @Value("${audio.apiSecret}")
    private String apiSecret;

    @Value("${audio.apiKey}")
    private String apiKey;

    @Value("${audio.StatusFirstFrame}")
    private int StatusFirstFrame;

    @Value("${audio.StatusContinueFrame}")
    private int StatusContinueFrame;

    @Value("${audio.StatusLastFrame}")
    private int StatusLastFrame;

}
