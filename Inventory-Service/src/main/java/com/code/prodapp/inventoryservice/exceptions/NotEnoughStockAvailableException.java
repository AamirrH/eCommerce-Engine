package com.code.prodapp.inventoryservice.exceptions;

public class NotEnoughStockAvailableException extends RuntimeException {
    public NotEnoughStockAvailableException(String message) {
        super(message);
    }
}
