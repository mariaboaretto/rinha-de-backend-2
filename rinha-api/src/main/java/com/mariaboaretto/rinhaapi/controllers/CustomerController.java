package com.mariaboaretto.rinhaapi.controllers;

import com.mariaboaretto.rinhaapi.domain.CustomerAccountInfoDTO;
import com.mariaboaretto.rinhaapi.domain.ExtractDTO;
import com.mariaboaretto.rinhaapi.domain.TransactionDTO;
import com.mariaboaretto.rinhaapi.exceptions.LimitExceededException;
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

    // TODO
    @PostMapping("/{id}/transacoes")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transaction, @PathVariable Integer id) {
       CustomerAccountInfoDTO updatedAccInfo;
       try {
           updatedAccInfo = this.customerService.addTransaction(id, transaction);
       } catch (LimitExceededException e) {
           return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
       } catch (DataIntegrityViolationException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Not Found");
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
        }

        return ResponseEntity.ok().body(extract);
    }
}
