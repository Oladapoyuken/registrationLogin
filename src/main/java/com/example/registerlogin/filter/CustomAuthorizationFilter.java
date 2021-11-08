package com.example.registerlogin.filter;

import com.example.registerlogin.security.JwtTokenProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProcessor jwtTokenProcessor;

    private List<String> excludedPath = List.of("/api/login", "/api/forget", "/api/register", "/api/confirm", "/view");

    public CustomAuthorizationFilter(JwtTokenProcessor jwtTokenProcessor) {
        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");// important
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT"); //might be excluded
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization"); //important

        if(isPathExcluded(request.getServletPath())){
            filterChain.doFilter(request, response);
        }
        else{
            String authHeader = request.getHeader(AUTHORIZATION);
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                String token = authHeader.substring("Bearer ".length());
                try {
                    Map<String, Object> credential = jwtTokenProcessor.decryptToken(token);
                    var username = (String) credential.get("username");
                    var authorities = (Collection<SimpleGrantedAuthority>) credential.get("authority");

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);

                }catch (Exception e){
                    authFailure(response);
                }
            }
            else{
                authFailure(response);
            }
        }

    }
    private void authFailure(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(SC_OK);
        Map<String, String> message = new HashMap<>();
        message.put("status", "expired");
        message.put("message", "Unauthorized resource access, please login");
        new ObjectMapper().writeValue(response.getOutputStream(), message);
    }

    private boolean isPathExcluded(String servletPath){
        return excludedPath.stream()
                .filter(path -> servletPath.startsWith(path))
                .findAny().isPresent();
    }
}
