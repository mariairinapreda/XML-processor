package org.example.service;

import org.example.model.Order;
import org.example.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RepositoryImplTest {
    static RepositoryImpl processDataStructures;


    @BeforeAll
    public static  void init(){
    processDataStructures= RepositoryImpl.getInstance();
    }



    @Test
    public void getInstanceShouldReturnNewProcessDataStructuresObject() {
    assertThat(processDataStructures).isInstanceOf(RepositoryImpl.class);
    }

    @Test
   public void getIdShouldReturnIdFromFilename() {
    Integer expectedId=44;
    Integer testedId=processDataStructures.getId("orders44.xml");
    assertThat(testedId).isEqualTo(expectedId);
    }

    public static List<Order> setOrders(){
        List<Order> orders=new ArrayList<>();
        Product product1=new Product();
        product1.setSupplier("sony");
        Product product2=new Product();
        product2.setSupplier("panasonic");
        Order order=new Order();
        order.setProducts(List.of(product1, product2));
        orders.add(order);
   return orders;
    }

    @Test
    public void getProductsBySupplierShouldReturnListOfProductWithGivenSupplier() {
        List<Product> products=processDataStructures.getProductsBySupplier("sony",setOrders());
        assertThat(products.get(0).getSupplier()).isEqualTo("sony");
    }

    @Test
    void getAllSuppliersShouldReturnListOfAllSuppliers() {
        List<Order> orders=setOrders();
        List<String> expectedResult=List.of("sony", "panasonic");
        List<String> testedSuppliers=processDataStructures.getAllSuppliers(orders);
        assertThat(testedSuppliers).isEqualTo(expectedResult);
    }
}