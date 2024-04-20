package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@UnitTest
public class MyUnitTests {

    @Mock
    private NotificationService notificationService;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks 
    private ProductService productService;

    @Test
    public void test() {
        // GIVEN
        Product product =new Product(null, 15, 0, "NORMAL", "RJ45 Cable", null, null, null , null, null, null);
        Product product1 =new Product(null, 10, 0, "FLASHSALE", "mouse", LocalDate.now().minusDays(2), LocalDate.now().plusDays(180), 10);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(productRepository.save(product1)).thenReturn(product1);

        // WHEN
        productService.notifyDelay(product.getLeadTime(), product);
        productService.handleFlashSaleProduct(product1);

        // THEN
        assertEquals(0, product.getAvailable());
        assertEquals(15, product.getLeadTime());
        assertEquals(9, product1.getFlashSaleLimitOrder());
        Mockito.verify(productRepository, Mockito.times(2)).save(product);
        Mockito.verify(notificationService, Mockito.times(1)).sendDelayNotification(product.getLeadTime(), product.getName());
    }
}