package com.example.BankingSystem.Service;

import com.example.BankingSystem.Entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//this annotation marked class in Spring bean
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter { //extends ensure filter HTTP request only one time
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal( //@NonNull are required parameters is not null
            @NonNull HttpServletRequest request, //representation for request from client (HTTP header, get/post method, Authorization)
            @NonNull HttpServletResponse response,//representation send response to client (status code)
            @NonNull FilterChain filterChain)//filter chain constant
            throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");//take jwt token from Authorization header of HTTP request to serve authentication filter
        if(jwtToken == null || !jwtService.isTokenValid(jwtToken.substring(7))){//check token is null ? and cut "Bearer " in first
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = jwtToken.startsWith("Bearer ") ?jwtToken.substring(7) : jwtToken;//token start with "Bearer " ? if their substring 7 index
        String subject = jwtService.extractSubject(jwtToken);
        User user = (User) userDetailsService.loadUserByUsername(subject);
        var context = SecurityContextHolder.getContext();//take SecurityContext from client's request (SecurityContext include in Principle || Credentials || Authorities)
        if(user != null && context.getAuthentication() == null){
            var authenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());//create object authentication after user's information verified
            authenticationToken.setDetails(request);//attached information of HTTP request in authentication
            context.setAuthentication(authenticationToken);//take authentication into SecurityContext
        }
        filterChain.doFilter(request,response);
    }
}
