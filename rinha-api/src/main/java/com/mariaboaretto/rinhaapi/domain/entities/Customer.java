package com.mariaboaretto.rinhaapi.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    private Integer id;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_limit")
    private int limit;

    @Column(name = "customer_balance")
    private int balance;
}
