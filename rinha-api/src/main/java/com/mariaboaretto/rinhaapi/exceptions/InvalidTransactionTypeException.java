package com.mariaboaretto.rinhaapi.exceptions;

public class InvalidTransactionTypeException extends RuntimeException {
    public InvalidTransactionTypeException() {};
    public InvalidTransactionTypeException(String message) {
        super(message);
    }
}
