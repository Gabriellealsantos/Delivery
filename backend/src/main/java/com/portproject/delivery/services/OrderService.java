package com.portproject.delivery.services;

import com.portproject.delivery.dtos.OrderDTO;
import com.portproject.delivery.dtos.ProductDTO;
import com.portproject.delivery.entities.Order;
import com.portproject.delivery.entities.Product;
import com.portproject.delivery.repositories.OrderRepository;
import com.portproject.delivery.repositories.ProductRepository;
import com.portproject.delivery.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<OrderDTO> findOrdersWithProducts() {
        List<Order> list = orderRepository.findOrdersWithProducts();

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum pedido encontrado");
        }

        return list.stream().map(x -> new OrderDTO(x)).collect(Collectors.toList());
    }
}
