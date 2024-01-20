package com.CloudPan.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET = "mortal"; // 用于签名的密钥
    private static final long EXPIRATION_TIME = 3600 * 24 * 7; // 有效时间 24 小时
    public  static String  generateToken(){
        try{
            return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME)) //设置过期时间
                .withIssuer("CloudPan") //设置签发者
                .sign(Algorithm.HMAC256(SECRET)); //签名

        }catch (JWTCreationException e){
          throw new RuntimeException(e);
        }
    }


    public static boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("CloudPan")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e){
            //Invalid signature/claims
            e.printStackTrace();
            return false;
        }
    }

    }


