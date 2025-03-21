package com.javaee.elderlycanteen.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.javaee.elderlycanteen.entity.TokenInfo;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class JWTUtils {

    /**
     * 生成token  header.payload.singature
     */
    private static final String SING = "Tongjijava09";


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

    public static TokenInfo getTokenInfo(String token){
        TokenInfo tokenInfo = new TokenInfo();
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
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

