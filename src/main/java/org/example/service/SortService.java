package org.example.service;

import org.example.model.Order;
import org.example.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class SortService {
    private static SortService instance = null;

    private SortService() {
    }

    public static SortService getInstance() {
        return instance != null ? instance : new SortService();
    }


    public List<Product> sortProducts(List<Product> products) {
        return products.stream().sorted((product1, product2) -> product2.getPrice().compareTo(product1.getPrice())).collect(Collectors.toList());
    }

    public List<Order> sortOrders(List<Order> orders) {
        return orders.stream().sorted((product1, product2) -> product2.getDate().compareTo(product1.getDate())).collect(Collectors.toList());
    }
}
