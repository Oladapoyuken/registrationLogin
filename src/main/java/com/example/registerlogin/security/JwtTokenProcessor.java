package com.example.registerlogin.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.registerlogin.helpers.Global;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class JwtTokenProcessor {

    private Algorithm algorithm = Algorithm.HMAC256(Global.JWT_SECRET_KEY.getBytes());

    public Map<String, String> encryptToken(User user, HttpServletRequest request){

        String accessToken = buildToken(user, request, 30);
//        String refreshToken = buildToken(user, request, 60);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("message", accessToken);
//        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public Map<String, Object> decryptToken(String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("authorities", authorities);

        return credentials;
    }

    private String buildToken(User user, HttpServletRequest request, int timer){
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + timer * 60 * 1000))
                .withIssuer(request.getRequestURI())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

    }
}
