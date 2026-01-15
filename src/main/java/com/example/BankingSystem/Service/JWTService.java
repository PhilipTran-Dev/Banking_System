package com.example.BankingSystem.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;
import java.util.Date;

@Service
public class JWTService {
    @Value("${jwtSecretKey}")
    private String jwtSecretKey;
    private static final long EXPIRATION_TIME = 86400000;

   public SecretKey generateKey(){
       byte[] keyBytes = jwtSecretKey.getBytes();
       return Keys.hmacShaKeyFor(keyBytes);
   }

   public String generateToken(String username){
       Date now = new Date();
       Date expiration = new Date(now.getTime()+EXPIRATION_TIME);

       return Jwts.builder()
               .subject(username)//who is owner this token
               .issuedAt(now)//record time when token generated
               .expiration(expiration)
               .signWith(generateKey())//avoid editing payload
               .compact();//packaging token to string
   }
    //Check if the JWT is valid
    public Claims extractClaims(String token) {
        return Jwts.parser()//prepare reader for jwt
                .verifyWith(generateKey())//check sign and ensure token is not edited
                .build()//gone parse the same builder
                .parseSignedClaims(token)//decode token (Base64 → JSON) || verify sign || check token is expired || ensure token is correct structure JWT
                .getPayload();//get payload(claims) from jwt
    }


    public String extractSubject(String token){
       return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token){
       return extractClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token) {
       return new Date().before(extractExpiration(token));
    }
}
