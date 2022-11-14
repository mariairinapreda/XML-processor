package org.example.repositories;

import org.example.model.Order;
import org.example.model.Product;

import java.util.List;

public interface Repository {
    Integer getId(String filename);

    List<Product> getProductsBySupplier(String supplier, List<Order> orders);

    List<String> getAllSuppliers(List<Order> orders);

}
