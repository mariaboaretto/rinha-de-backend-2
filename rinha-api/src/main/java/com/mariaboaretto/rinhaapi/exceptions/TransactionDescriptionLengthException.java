package com.mariaboaretto.rinhaapi.exceptions;

public class TransactionDescriptionLengthException extends RuntimeException {
    public TransactionDescriptionLengthException() {};
    public TransactionDescriptionLengthException(String message) {
        super(message);
    }
}
