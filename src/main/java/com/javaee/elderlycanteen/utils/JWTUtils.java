package com.javaee.elderlycanteen.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.javaee.elderlycanteen.entity.TokenInfo;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class JWTUtils {

    /**
     * 生成token  header.payload.singature
     */
    private static final String SING = "Tongjijava09";

    /**
     * SEC-LAB-B1：统一身份平台的 RSA 公钥（仅测试密钥对）。
     */
    private static final String PUBLIC_KEY_PEM = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwfOouVHulgwMYEuiBoR4
            3gMGWkeYOYssKVZ5HM8JWjtjGJoenZcv9RiT8qQKN7DBiCv3vzsK7v6PPjifzSXH
            BIQFhHzbJt08eUDI6Gg2d656YLZG9o5u3gR/6NtRGRv9j0J3ZgWED24ti4W4PCAn
            On/InZZXE0mgJMZ6C8fEnWhppj+wfl5Ofk7HSpCI/1Ff9F2W3VBiOeUMBQVEzBNb
            NeZalRf0U0gQNv9FF6T63IeTsc5X28paGyGJ5hGm3NTzuyt/luDpN1lpoWJiSuOQ
            j77Ni3pUzqtqwpkDPKdN9i8KB5RwiG3H4ggDRPX1DrXSnapeymvS/kNi1qJQcft1
            mwIDAQAB
            -----END PUBLIC KEY-----
            """;


    public static String getToken(Map<String, String> map) {

        Calendar instance = Calendar.getInstance();
        // 默认7天过期
        instance.add(Calendar.DATE, 7);

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        // payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        String token = builder.withExpiresAt(instance.getTime())  //指定令牌过期时间
                .sign(Algorithm.HMAC256(SING));  // sign
        return token;
    }

    /**
     * 验证token  合法性
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    /**
     * SEC-LAB-B1：兼容 HS256 旧 token 与 RS256 统一身份平台。
     *
     * 漏洞点：
     * 1. 算法由 token header 的 alg 决定；
     * 2. HS256 验签失败后，回退到“用 RSA 公钥字节做 HMAC 密钥”；
     * 3. RSA 公钥是公开的，攻击者可以自己签 HS256 token。
     */
    public static DecodedJWT verifyFlexible(String token) {
        DecodedJWT decoded = JWT.decode(token);
        String alg = decoded.getAlgorithm();

        if ("RS256".equals(alg)) {
            return JWT.require(Algorithm.RSA256((RSAPublicKey) publicKey(), null))
                    .build()
                    .verify(token);
        }
        if ("HS256".equals(alg)) {
            try {
                return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
            } catch (SignatureVerificationException e) {
                return JWT.require(Algorithm.HMAC256(publicKey().getEncoded()))
                        .build()
                        .verify(token);
            }
        }
        throw new JWTVerificationException("unsupported algorithm: " + alg);
    }

    public static String getPublicKeyPem() {
        return PUBLIC_KEY_PEM;
    }

    private static PublicKey publicKey() {
        try {
            String normalized = PUBLIC_KEY_PEM
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] der = Base64.getDecoder().decode(normalized);
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(der));
        } catch (Exception e) {
            throw new IllegalStateException("invalid JWT public key", e);
        }
    }

    public static TokenInfo getTokenInfo(String token){
        TokenInfo tokenInfo = new TokenInfo();
        DecodedJWT verify = verifyFlexible(token);
        String accountIdString = verify.getClaim("accountId").asString();
        Integer accountId = Integer.parseInt(accountIdString);
        String accountName = verify.getClaim("accountname").asString();

        String identity = verify.getClaim("identity").asString();
        tokenInfo.setAccountId(accountId);
        tokenInfo.setAccountName(accountName);
        tokenInfo.setIdentity(identity);
        return tokenInfo;
    }
    /**
     * 获取token信息方法
     */
    /*public static DecodedJWT getTokenInfo(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return verify;
    }*/
}

