package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AccountDao;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.minio.MinioService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

/**
 * 远程头像导入。
 *
 * SEC-LAB-A2：URL 由请求方提供，服务端用 OkHttp 发起请求。
 * 这里只做了 scheme 和精确 host 黑名单，无法挡住短 IP、IPv6、
 * DNS rebinding 或重定向到内网地址。
 */
@Service
public class RemoteAvatarService {

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final Set<String> BLOCKED_HOSTS =
            Set.of("localhost", "127.0.0.1", "0.0.0.0");

    private final MinioService minioService;
    private final AccountDao accountDao;

    public RemoteAvatarService(MinioService minioService, AccountDao accountDao) {
        this.minioService = minioService;
        this.accountDao = accountDao;
    }

    public String importAvatar(String url, Integer accountId) throws Exception {
        if (url == null || url.isBlank()) {
            throw new ServiceException("url is empty");
        }

        URI uri;
        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("invalid url");
        }

        String scheme = uri.getScheme();
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new ServiceException("only http/https is supported");
        }

        String host = uri.getHost();
        if (host == null || BLOCKED_HOSTS.contains(host.toLowerCase())) {
            throw new ServiceException("host is not allowed");
        }

        Request request = new Request.Builder().url(url).build();
        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new ServiceException("download failed: " + response.code());
            }
            byte[] bytes = response.body().bytes();
            String contentType = response.header("Content-Type");
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            String key = "avatar/import-" + UUID.randomUUID() + ".img";
            String fileUrl = minioService.uploadBytes(key, bytes, contentType);
            accountDao.updatePortrait(fileUrl, accountId);
            return fileUrl;
        }
    }
}
