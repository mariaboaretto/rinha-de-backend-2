package com.mariaboaretto.rinhaapi.controllers;

import com.mariaboaretto.rinhaapi.domain.CustomerAccountInfoDTO;
import com.mariaboaretto.rinhaapi.domain.ExtractDTO;
import com.mariaboaretto.rinhaapi.domain.TransactionDTO;
import com.mariaboaretto.rinhaapi.exceptions.InvalidTransactionTypeException;
import com.mariaboaretto.rinhaapi.exceptions.LimitExceededException;
import com.mariaboaretto.rinhaapi.exceptions.TransactionDescriptionLengthException;
import com.mariaboaretto.rinhaapi.exceptions.UserNotFoundException;
import com.mariaboaretto.rinhaapi.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/clientes")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/{id}/transacoes")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transaction, @PathVariable Integer id) {
       CustomerAccountInfoDTO updatedAccInfo;
       try {
           updatedAccInfo = this.customerService.addTransaction(id, transaction);
       } catch (LimitExceededException e) {
           // If transaction exceeds customer's limit, return 422
           return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
       } catch (DataIntegrityViolationException e) {
           // If customer does not exist, return 404
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Not Found");
       } catch (InvalidTransactionTypeException e) {
           // If transaction type is invalid, return 422
           return  ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
       } catch (TransactionDescriptionLengthException e) {
           // If transaction description length is invalid, return 422
           return  ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
       } catch (Exception e) {
           // Deals with any other exception
           return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }

        return ResponseEntity.ok().body(updatedAccInfo);
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<?> getExtract(@PathVariable Integer id) {
        ExtractDTO extract = null;

        // If user doesn't exist, return 404 with error message
        try {
            extract = this.customerService.getExtract(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        } catch (Exception e) {
            // Handles any other exception
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok().body(extract);
    }
}
