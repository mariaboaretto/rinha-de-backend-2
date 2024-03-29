package com.mariaboaretto.rinhaapi.services;

import com.mariaboaretto.rinhaapi.domain.CustomerAccountInfoDTO;
import com.mariaboaretto.rinhaapi.domain.ExtractDTO;
import com.mariaboaretto.rinhaapi.domain.TransactionDTO;
import com.mariaboaretto.rinhaapi.exceptions.InvalidTransactionTypeException;
import com.mariaboaretto.rinhaapi.exceptions.LimitExceededException;
import com.mariaboaretto.rinhaapi.exceptions.TransactionDescriptionLengthException;
import com.mariaboaretto.rinhaapi.exceptions.UserNotFoundException;
import com.mariaboaretto.rinhaapi.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public ExtractDTO getExtract(Integer customerId) throws UserNotFoundException, SQLException {
        // Getting data to generate extract
        var extractData = this.customerRepository.getExtractData(customerId);

        return new ExtractDTO(extractData);
    }

    @Transactional
    // Adds new transaction to DB and updates customer's balance accordingly
    public CustomerAccountInfoDTO addTransaction(Integer customerId, TransactionDTO transaction) {
        // Validates if transaction type is valid
        if (transaction.getTipo() != 'c' && transaction.getTipo() != 'd') {
            throw new InvalidTransactionTypeException("Transaction must be of type 'c' or 'd'");
        }

        // Validates if transaction description complies with length limitation
        if (transaction.getDescricao() == null ||
                transaction.getDescricao().length() > 10 ||
                transaction.getDescricao().isEmpty()) {
            throw new TransactionDescriptionLengthException("Transaction description length must be between 1-10 characters.");
        }

        // Attempts to update customer's balance
        try {
            var amount = (transaction.getTipo() == 'c') ? (transaction.getValor()) : (transaction.getValor() * -1);
            this.customerRepository.updateCustomerBalance(customerId, amount);
        } catch (Exception e) {
            throw new LimitExceededException("Customer Limit Exceeded");
        }

        // Adds transaction to DB
        this.customerRepository.createTransaction(customerId, transaction.getDescricao(), transaction.getValor(),
                transaction.getTipo());

        // Returning account's update info
        return this.customerRepository.getAccountInfo(customerId);
    }
}
