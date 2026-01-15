package com.example.BankingSystem.Repository;

import com.example.BankingSystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepos extends JpaRepository<User,String> {
    Optional<User> findByUsernameIgnoreCase (String username);
}
