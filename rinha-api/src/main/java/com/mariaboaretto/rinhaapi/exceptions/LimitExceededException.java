package com.mariaboaretto.rinhaapi.exceptions;

public class LimitExceededException extends RuntimeException{
    public LimitExceededException() {}

    public LimitExceededException(String message) {
        super(message);
    }

}
