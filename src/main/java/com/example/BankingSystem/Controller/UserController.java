package com.example.BankingSystem.Controller;

import com.example.BankingSystem.DTO.UserDTO;
import com.example.BankingSystem.Entity.User;
import com.example.BankingSystem.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //ResponseEntity<T>:
    //- represents HTTP response
    //- allows control status code & headers
    //- used at controller layer
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO){//requestBody bridge between HTTP JSON and Java object
        return ResponseEntity.ok(userService.registerUser(userDTO));//set HTTP status = 200 || Attach body = user
    }

    //ResponseEntity<?> method return responseEntity with body of any types. Use this when API have many types response (JSON)
    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDTO userDTO){
        var authObject = userService.authenticateUser(userDTO);//var is a keyword to complier, determined clearly data type
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, (String) authObject.get("token"))
                .body(authObject.get("user"));
    }



}
