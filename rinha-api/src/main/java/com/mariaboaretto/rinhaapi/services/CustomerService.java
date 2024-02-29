package com.mariaboaretto.rinhaapi.services;

import com.mariaboaretto.rinhaapi.domain.CustomerAccountInfoDTO;
import com.mariaboaretto.rinhaapi.domain.ExtractDTO;
import com.mariaboaretto.rinhaapi.domain.TransactionDTO;
import com.mariaboaretto.rinhaapi.exceptions.LimitExceededException;
import com.mariaboaretto.rinhaapi.exceptions.UserNotFoundException;
import com.mariaboaretto.rinhaapi.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public ExtractDTO getExtract(Integer customerId) throws UserNotFoundException {
        // Getting necessary data to generate extract
        var extractData = this.customerRepository.getExtractData(customerId);

        // Validating if customer exists. If not, throw UserNotFound exception
        if (extractData.isEmpty()) throw new UserNotFoundException("User Not Found");

        return new ExtractDTO(extractData);
    }

    @Transactional
    // Adds new transaction to DB and updates customer's balance accordingly
    public CustomerAccountInfoDTO addTransaction(Integer customerId, TransactionDTO transaction) {
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

        // Returning account's update info TODO Review
        return this.customerRepository.getAccountInfo(customerId);
    }

    // Updates customer balance
    private void updateCustomerBalance(Integer id, Integer amount) {
        this.customerRepository.updateCustomerBalance(id, amount);
    }
}
