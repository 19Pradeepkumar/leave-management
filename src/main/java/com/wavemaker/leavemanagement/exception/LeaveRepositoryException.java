package com.wavemaker.leavemanagement.exception;

public class LeaveRepositoryException extends RuntimeException {

    public LeaveRepositoryException(String message) {
        super(message);
    }

    public LeaveRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
