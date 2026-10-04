package com.mini_commerce.product_service.dto;

public class StockUpdateRequest {

    private int quantity;

    public StockUpdateRequest() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}