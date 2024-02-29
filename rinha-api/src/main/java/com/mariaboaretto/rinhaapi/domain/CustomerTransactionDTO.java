package com.mariaboaretto.rinhaapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTransactionDTO {
    private String transaction_description;
    private Integer transaction_amount;
    private char transaction_type;
    private Timestamp transaction_date;
    private Integer customer_balance;
    private Integer customer_limit;
}
