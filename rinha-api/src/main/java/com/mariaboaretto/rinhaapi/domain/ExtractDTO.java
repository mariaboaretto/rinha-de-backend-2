package com.mariaboaretto.rinhaapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractDTO {
    private CustomerExtractInfo saldo;
    List<TransactionDTO> ultimas_transacoes = new ArrayList<TransactionDTO>();

    public ExtractDTO(List<CustomerTransactionDTO> extractData) {
        // Setting balance information
        this.saldo = new CustomerExtractInfo(extractData.get(0).getCustomer_balance(),
                LocalDateTime.now(), extractData.get(0).getCustomer_limit());

        // Setting transaction list if customer has any transactions
        if (extractData.get(0).getTransaction_description() != null) {
            // Populating transaction list
            for (CustomerTransactionDTO data : extractData) {
                TransactionDTO transaction = new TransactionDTO(data.getTransaction_amount(), data.getTransaction_type(),
                        data.getTransaction_description(), data.getTransaction_date());

                ultimas_transacoes.add(transaction);
            }
        }
    }
}
