package com.cesarp.bankingservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="account_number", nullable = false, unique = true)
    private String acoountNumber;
    @Column(name="balance", nullable = false)
    private BigDecimal balance;
    @Column(name="owner_name", nullable = false)
    private String ownerName;
    @Column(name="owner_email", nullable = false)
    private String ownerEmail;
    @Column(name="created_at")
    private LocalDate createAt;
    @Column(name="update_at")
    private LocalDate updateAt;
}
