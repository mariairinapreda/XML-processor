package org.example.service;

import org.example.model.Order;
import org.example.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SortServiceTest {
    private static SortService sortLists;

    @BeforeAll
    public static void initialize(){
        sortLists= SortService.getInstance();
    }
    @Test
    void getInstanceShouldReturnInstanceOfSortLists() {
        assertThat(sortLists).isInstanceOf(SortService.class);
    }

    @Test
    void sortProductsShouldReturnInOfProductsInOrderByPrice() {
        List<Product> productList=new ArrayList<>();
        Product product=new Product();
        product.setPrice((double)10);
        Product product1=new Product();
        product1.setPrice((double)5);
        productList.add(product);
        productList.add(product1);
        List<Product> testList=sortLists.sortProducts(productList);
        List<Product> expected=List.of(product, product1);
        assertThat(testList).isEqualTo(expected);
    }

    @Test
    void sortOrdersShouldReturnListOfOrdersOrderedByCreationDate() {
    Order order=new Order();
    order.setDate(LocalDateTime.parse("2012-07-12T15:29:33.000"));
    Order order1=new Order();
    order1.setDate(LocalDateTime.parse("2014-07-12T15:29:33.000"));
    List<Order> testOrders=sortLists.sortOrders(List.of(order, order1));
    List<Order> expectedOrder=List.of(order1, order);
    assertThat(testOrders).isEqualTo(expectedOrder);
    }
}