package org.example.service;

import org.example.model.Order;
import org.example.model.Product;
import org.example.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RepositoryImpl implements Repository {

    private static RepositoryImpl instance = null;

    private RepositoryImpl() {
    }


    public static RepositoryImpl getInstance() {
        return instance != null ? instance : new RepositoryImpl();
    }

    @Override
    public Integer getId(String fileName) {
        return Integer.parseInt(fileName.substring(6,8));
    }

    @Override
    public List<Product> getProductsBySupplier(String supplier, List<Order> orders) {
        SortService sortLists = SortService.getInstance();
        List<Product> products = new ArrayList<>();
        for (Order order : orders) {
            for (Product product : order.getProducts()) {
                if (Objects.equals(product.getSupplier(), supplier)) {
                    products.add(product);
                }
            }
        }
        return sortLists.sortProducts(products);
    }

    @Override
    public List<String> getAllSuppliers(List<Order> orders) {
        List<String> suppliers = new ArrayList<>();
        for (Order order : orders) {
            for (Product product : order.getProducts()) {
                if (!suppliers.contains(product.getSupplier())) {
                    suppliers.add(product.getSupplier());
                }
            }
        }
        return suppliers;
    }
}
