package com.nimbleways.springboilerplate.contollers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class MyController {
    @Autowired
    private ProductService ps;

    @Autowired
    private ProductRepository pr;

    @Autowired
    private OrderRepository or;

    @PostMapping("{orderId}/processOrder")
    @ResponseStatus(HttpStatus.OK)
    public ProcessOrderResponse processOrder(@PathVariable Long orderId) {
        Order order = or.findById(orderId).get();
        System.out.println(order);
        List<Long> ids = new ArrayList<>();
        ids.add(orderId);
        Set<Product> products = order.getItems();
        for (Product p : products) {
            if (p.getType().equals("NORMAL")) {

                ps.handleNormalProduct(p);

            } else if (p.getType().equals("SEASONAL")) {

                ps.handleSeasonalProduct(p);

            } else if (p.getType().equals("EXPIRABLE")) {

                ps.handleExpiredProduct(p);

            } else if (p.getType().equals("FLASHSALE")) {

                ps.handleFlashSaleProduct(p);
            }
        }

        return new ProcessOrderResponse(order.getId());
    }
}
