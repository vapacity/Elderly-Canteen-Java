package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.AudioService;
import com.javaee.elderlycanteen.service.CartItemService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("api/audio")
public class AudioController {

    @Autowired
    private AudioService audioService;
    private CartItemService cartItemService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAudio(@Parameter(name = "上传的图片", required = true)
                                                  @RequestParam("audio") MultipartFile audioFile,int cartId) {
        try {
            // 调用服务层方法将音频转为文字
            String transcription = audioService.processAudioFile(audioFile,cartId);
            return ResponseEntity.ok(transcription); // 返回转写结果
        } catch (Exception e) {
            return ResponseEntity.status(500).body("音频转文字失败: " + e.getMessage());
        }
    }
}
