//package com.example.registerlogin.filter;
//
//import com.example.registerlogin.model.Response;
//import com.example.registerlogin.security.JwtTokenProcessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//
//    private final JwtTokenProcessor jwtTokenProcessor;
//
//    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProcessor jwtTokenProcessor) {
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenProcessor = jwtTokenProcessor;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//
//        return authenticationManager.authenticate(authenticationToken);
//
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        User user = (User) authResult.getPrincipal();
//        var token = jwtTokenProcessor.encryptToken(user, request);
//
//        response.setContentType(APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), token);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        response.setContentType(APPLICATION_JSON_VALUE);
//        Response message = new Response("failed", "Invalid login credentials");
//        new ObjectMapper().writeValue(response.getOutputStream(), message);
//    }
//}
