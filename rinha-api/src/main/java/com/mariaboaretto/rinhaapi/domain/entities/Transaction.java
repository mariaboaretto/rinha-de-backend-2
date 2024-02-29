package com.mariaboaretto.rinhaapi.domain.entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private Integer id;

    @Column(name = "transaction_value")
    private Integer amount;

    @Column(name = "transaction_type")
    private char type;

    @Column(name = "transaction_description")
    private String description;

    @Column(name = "transaction_date")
    private Date date;

    @Column(name = "customer_id")
    private Integer customerId;
}
