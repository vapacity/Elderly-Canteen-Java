package com.javaee.elderlycanteen.minio;


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
            @RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        System.out.println("Request received");
        System.out.println("File name: " + file.getOriginalFilename());
        System.out.println("File size: " + file.getSize());
        try {

            System.out.println("start");
            minioService.uploadFile(fileName, file);
            System.out.println("end");
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