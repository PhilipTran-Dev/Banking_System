package com.example.BankingSystem.Configuration;

import com.example.BankingSystem.Repository.UserRepos;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepos userRepos;

    //From UserRepos to make it
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepos.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username is not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //DAO = Data Access Object
    //combined by UserDetailsService and PasswordEncoder
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    //This bean exposes the AuthenticationManager that is internally built and configured by Spring Security.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    //take api from outside
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


     //Single-thread scheduled executor.
     // Ensures scheduled tasks execute sequentially to avoid concurrency issues.
     //Suitable for lightweight and periodic background jobs.
    @Bean
    public ScheduledExecutorService scheduledExecutorService(){
        return Executors.newScheduledThreadPool(1);
    }
}
