package com.striker.ecommerce.order_service.service;

import com.striker.ecommerce.order_service.dto.OrderRequestDto;
import com.striker.ecommerce.order_service.dto.OrderRequestItemDto;
import com.striker.ecommerce.order_service.entity.Orders;
import com.striker.ecommerce.order_service.repository.OrdersRepository;
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
}
