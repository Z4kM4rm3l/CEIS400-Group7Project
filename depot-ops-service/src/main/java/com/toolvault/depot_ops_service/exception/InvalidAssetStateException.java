package com.toolvault.depot_ops_service.exception;

public class InvalidAssetStateException extends RuntimeException {
    public InvalidAssetStateException(String message) {
        super(message);
    }
}