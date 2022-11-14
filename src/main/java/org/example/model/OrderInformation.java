package org.example.model;

import java.util.List;

public class OrderInformation {
    private Integer ID;
    private List<Order> orders;

    public OrderInformation() {
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
