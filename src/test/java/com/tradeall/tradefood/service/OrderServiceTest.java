package com.tradeall.tradefood.service;

import com.tradeall.tradefood.entity.Order;
import com.tradeall.tradefood.entity.User;
import com.tradeall.tradefood.repository.OrderRepository;
import com.tradeall.tradefood.repository.ProductRepository;
import com.tradeall.tradefood.repository.UserRepository;
import com.tradeall.tradefood.repository.PaymentRepository;
import com.tradeall.tradefood.client.SellsyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private SellsyClient sellsyClient;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, sellsyClient, productRepository, userRepository, paymentRepository);
    }

    @Test
    void validateOrderWithCode_Success() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String code = "1234";
        
        User user = new User();
        user.setId(userId);
        
        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setValidationCode(code);
        order.setStatus(Order.OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Order validatedOrder = orderService.validateOrderWithCode(orderId, userId, code);

        // Then
        assertTrue(validatedOrder.isValidated());
        assertEquals(Order.OrderStatus.DELIVERED, validatedOrder.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void validateOrderWithCode_WrongUser() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID wrongUserId = UUID.randomUUID();
        String code = "1234";
        
        User user = new User();
        user.setId(userId);
        
        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setValidationCode(code);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            orderService.validateOrderWithCode(orderId, wrongUserId, code)
        );
    }

    @Test
    void validateOrderWithCode_WrongCode() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String correctCode = "1234";
        String wrongCode = "0000";
        
        User user = new User();
        user.setId(userId);
        
        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setValidationCode(correctCode);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            orderService.validateOrderWithCode(orderId, userId, wrongCode)
        );
    }
}
