package com.mobilefintech09.lookwides.entities;

import com.squareup.moshi.Json;

import java.util.List;

public class OrderResponse {
    @Json(name = "order")
    private Order order;
    @Json(name = "message")
    private String message;

    /**
     * No args constructor for use in serialization
     *
     */
    public OrderResponse() {
    }

    /**
     *
     * @param message
     * @param order
     */
    public OrderResponse(Order order, String message) {
        super();
        this.order = order;
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
