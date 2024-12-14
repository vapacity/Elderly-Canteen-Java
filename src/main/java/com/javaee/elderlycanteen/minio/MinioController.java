package com.javaee.elderlycanteen.minio;


import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.entity.TokenInfo;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.service.AccountService;
import com.javaee.elderlycanteen.utils.JWTUtils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MinioController {

    @Autowired
    private MinioService minioService;


    @PostMapping("/upload")
    public String uploadFile(
            @RequestBody(
                    required = true,
                    content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam("file") MultipartFile file,
            @RequestHeader(name = "Authorization", required = false)String token) {
        // 获取accountId
        if( token == null ){
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();
        String fileName = file.getOriginalFilename();
        System.out.println("Request received");
        System.out.println("File name: " + file.getOriginalFilename());
        System.out.println("File size: " + file.getSize());
        try {
            minioService.uploadFile(fileName, file);
            System.out.println("end");
            minioService.updatePortrait(fileName,accountId);

            return "File uploaded successfully: " ;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while uploading file: " + e.getMessage();
        }
    }
    @GetMapping("/test")
    public String test() {
        System.out.println("test");
        return "hello";
    }
}