package com.example.BankingSystem.DTO;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@Builder//convenient for service layer create more readable object
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String tag;
    private String gender;
    private Date dob;
    private Long tel;
}
