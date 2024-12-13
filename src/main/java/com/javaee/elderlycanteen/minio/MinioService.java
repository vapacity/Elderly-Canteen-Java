package com.javaee.elderlycanteen.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;


    private MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String uploadFile(String fileName, MultipartFile file) throws Exception {
        MinioClient minioClient = getMinioClient();
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1) // -1 表示未知的对象大小
                            .contentType(file.getContentType())
                            .build()
            );
            String fileUrl = getFileUrl(fileName);
            return fileUrl;
        }
    }
    private String getFileUrl(String fileName) {
        return "https://" + endpoint + "/" + bucketName + "/" + fileName;
    }

}
