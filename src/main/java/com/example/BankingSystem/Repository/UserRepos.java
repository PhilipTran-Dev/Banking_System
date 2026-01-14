package com.example.BankingSystem.Repository;

import com.example.BankingSystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepos extends JpaRepository<User,String> {

}
