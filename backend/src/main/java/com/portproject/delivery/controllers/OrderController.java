package com.portproject.delivery.controllers;

import com.portproject.delivery.dtos.OrderDTO;
import com.portproject.delivery.dtos.ProductDTO;
import com.portproject.delivery.services.OrderService;
import com.portproject.delivery.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> findOrdersWithProducts() {
        List<OrderDTO> list = orderService.findOrdersWithProducts();
        return ResponseEntity.ok().body(list);
    }
}
