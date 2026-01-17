package com.example.BankingSystem.Configuration;

import com.example.BankingSystem.Filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
//this annotation enables Spring Security to allow custom security configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)// Disable CSRF since the application is stateless and uses JWT authentication
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))//cors is Cross Origin Resources Sharing
                .authorizeHttpRequests((request) -> //configuration authorization rules for http requests
                        request.requestMatchers("/user/auth", "/user/register")//determined url or endpoint is applied following rules
                                .permitAll()//permission for all
                                .anyRequest()//permission for all remaining requests
                                .authenticated()//user have been authenticated
                )
                .authenticationProvider(authenticationProvider)//authenticate machine is check information || credentials || and return if success
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)// Ensure JWT authentication is processed before Spring Security evaluates authentication state
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));// Disable HTTP session creation and enforce stateless authentication for JWT-based APIs
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");//endpoints or url are accessed
        corsConfiguration.addAllowedMethod("*");//method is used Get || Post
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);// Allow all origins; endpoint mapping is controlled by UrlBasedCorsConfigurationSource
        return urlBasedCorsConfigurationSource;
    }
}
