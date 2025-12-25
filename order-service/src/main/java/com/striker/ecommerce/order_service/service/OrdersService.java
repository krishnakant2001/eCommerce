package com.striker.ecommerce.order_service.service;

import com.striker.ecommerce.order_service.clients.InventoryOpenFeignClient;
import com.striker.ecommerce.order_service.dto.OrderRequestDto;
import com.striker.ecommerce.order_service.dto.OrderRequestItemDto;
import com.striker.ecommerce.order_service.entity.OrderItem;
import com.striker.ecommerce.order_service.entity.OrderStatus;
import com.striker.ecommerce.order_service.entity.Orders;
import com.striker.ecommerce.order_service.repository.OrdersRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;

    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");

        List<Orders> orders = ordersRepository.findAll();
        return orders.stream()
                .map(product -> modelMapper.map(product, OrderRequestDto.class))
                .toList();
    }

    public OrderRequestDto getOrderById(Long id) {
        log.info("Getting order from their Id: {}", id);
        Optional<Orders> order = ordersRepository.findById(id);
        return order.map(product -> modelMapper.map(product, OrderRequestDto.class))
                .orElseThrow(() -> new RuntimeException("Inventory not Found"));

    }

    @Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("Calling the createOrder method....");
        Double totalPrice = inventoryOpenFeignClient.reduceStocks(orderRequestDto);

        Orders orders = modelMapper.map(orderRequestDto, Orders.class);
        for(OrderItem orderItem : orders.getItems()) {
            orderItem.setOrder(orders);
        }

        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);

        Orders savedOrder = ordersRepository.save(orders);

        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto, Throwable throwable) {
        log.error("Fallback error occurred due to: {}", throwable.getMessage());

        return new OrderRequestDto();
    }
}
