package com.belanjaki.id.jwt;

import com.belanjaki.id.common.constant.ReturnCode;
import com.belanjaki.id.common.dto.BaseResponse;
import com.belanjaki.id.common.dto.ErrorObjectDTO;
import com.belanjaki.id.common.dto.Meta;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JWTUtils {


    // token JWT yang dihasilkan akan berlaku selama 1 hari
    private static final long JWT_TOKEN_VALID = 24 * 60 * 60 * 1000L;

    // buat test ( token 5 menit )
    //private static final long JWT_TOKEN_VALID = 5 * 60;

    @Value("${secretkey}")
    private String SECRET_KEY;

    public String getAppIdFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInMethod())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("app_id", String.class);
    }

    // extract username from result extractClaim
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // extract claims from JWT Token -> inside payload  ( header.payload.signature )
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // parsing all JWT Token for get secret keys
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInMethod())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // result key for use on process signature and verify token
    private Key getSignInMethod() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails, UUID role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("app_id", role);
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> extraClaims, String subject) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALID * 1000))
                .signWith(getSignInMethod(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public void generateResponseIfHaveBeenLogout(HttpServletResponse httpResponse) throws IOException {
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setStatus(HttpServletResponse.SC_OK);

        ErrorObjectDTO dto = new ErrorObjectDTO();
        dto.setMessage(ReturnCode.UNAUTHORIZED.getMessage());
        dto.setStatusCode(ReturnCode.UNAUTHORIZED.getStatusCode());
        dto.setTimeStamp(new Date());

        BaseResponse<ErrorObjectDTO> baseResponse = new BaseResponse<>(dto, new Meta(ReturnCode.UNAUTHORIZED.getStatusCode(), HttpStatus.UNAUTHORIZED.getReasonPhrase()));

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(httpResponse.getOutputStream(), baseResponse.getCustomizeResponse("unauthorized"));
    }

    public void generateResponseAccessDenied(HttpServletResponse httpResponse)throws IOException{
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setStatus(HttpServletResponse.SC_OK);

        ErrorObjectDTO dto = new ErrorObjectDTO();
        dto.setMessage(ReturnCode.NOT_ADMIN.getMessage());
        dto.setStatusCode(ReturnCode.NOT_ADMIN.getStatusCode());
        dto.setTimeStamp(new Date());

        BaseResponse<ErrorObjectDTO> baseResponse = new BaseResponse<>(dto, new Meta(ReturnCode.NOT_ADMIN.getStatusCode(), ReturnCode.NOT_ADMIN.getMessage()));

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(httpResponse.getOutputStream(), baseResponse.getCustomizeResponse("access_denied"));
    }
}
