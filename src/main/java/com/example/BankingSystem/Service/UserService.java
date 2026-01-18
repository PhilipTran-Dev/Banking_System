package com.example.BankingSystem.Service;

import com.example.BankingSystem.DTO.UserDTO;
import com.example.BankingSystem.Entity.User;
import com.example.BankingSystem.Repository.UserRepos;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepos userRepos;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    //this method decision all
    public User registerUser(UserDTO userDTO){
        User user = mapToUser(userDTO);
        return userRepos.save(user);
    }

    //this method connect with registerUser and behind this process business logic
    public User mapToUser(UserDTO userDTO){
       return User.builder()
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .tag("client_" + userDTO.getTag())
                .gender(userDTO.getGender())
                .dob(userDTO.getDob())
                .tel(userDTO.getTel())
                .roles(List.of("USER"))// system-owned decision
                .build();
    }

    //Object data type which can be contained multiple different data type within the same response
    //String contain token JWT
    // Declare as Map to follow "program to an interface" principle
    public Map<String, Object> authenticateUser(UserDTO userDTO){//Map<> is a interface
        Map<String,Object> authObject = new HashMap<String, Object>();//HashMap<> is actually contain data in there
        User user = (User) userDetailsService.loadUserByUsername(userDTO.getUsername());
        if(user==null){
            throw new UsernameNotFoundException("User name is not found");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(),userDTO.getPassword()));
        authObject.put("token","Bearer".concat(jwtService.generateToken(userDTO.getUsername())));//concat is function connect chain || Bearer is not token this is a authentication mechanism || Token that real authentic data
        authObject.put("user",user);//attach user's information when response
        return authObject;
    }

}
