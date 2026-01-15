package com.example.BankingSystem.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;
    private double balance;
    private String accountName;
    @Column(nullable = false,unique = true)
    private long accountNumber;
    private String currency;
    private String code;
    private String label;
    private char symbol;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Status status;
    private Type type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Transactions> transactions;


}
