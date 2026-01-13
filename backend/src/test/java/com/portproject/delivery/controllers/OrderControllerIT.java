package com.portproject.delivery.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portproject.delivery.dtos.OrderDTO;
import com.portproject.delivery.dtos.ProductDTO;
import com.portproject.delivery.entities.Order;
import com.portproject.delivery.entities.Product;
import com.portproject.delivery.entities.enums.OrderStatus;
import com.portproject.delivery.repositories.OrderRepository;
import com.portproject.delivery.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de QUALIDADE para OrderController
 * Total: 5 testes de integração REST
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerIT {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private Product pizza;
        private Product refrigerante;

        @BeforeEach
        void setUp() {
                orderRepository.deleteAll();
                productRepository.deleteAll();
                pizza = productRepository.save(new Product(null, "Pizza", 45.90, "Margherita", "uri1"));
                refrigerante = productRepository.save(new Product(null, "Refrigerante", 8.00, "Cola", "uri2"));
        }

        @Test
        @DisplayName("GET /orders - Deve retornar 200 com pedidos PENDENTES")
        void deveRetornar200_ComPedidosPendentes() throws Exception {
                // Arrange
                criarPedidoPendente();

                // Act & Assert
                mockMvc.perform(get("/orders"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].status", is("PENDING")));
        }

        @Test
        @DisplayName("GET /orders - Deve retornar 404 quando não há pendentes")
        void deveRetornar404_QuandoNaoHaPendentes() throws Exception {
                // Arrange - criar pedido ENTREGUE
                Order entregue = new Order("Rua", -12.0, -38.0, Instant.now(), OrderStatus.DELIVERED);
                entregue.getProducts().add(pizza);
                orderRepository.save(entregue);

                // Act & Assert
                mockMvc.perform(get("/orders"))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("POST /orders - Pedido criado deve ter status PENDING")
        void pedidoCriado_DeveTerStatusPending() throws Exception {
                // Arrange
                OrderDTO dto = criarOrderDTO();

                // Act & Assert
                mockMvc.perform(post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status", is("PENDING")));
        }

        @Test
        @DisplayName("PUT /orders/{id}/delivered - Deve mudar para DELIVERED")
        void deveRetornar200_ComStatusDelivered() throws Exception {
                // Arrange
                Order pedido = criarPedidoPendente();

                // Act & Assert
                mockMvc.perform(put("/orders/{id}/delivered", pedido.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status", is("DELIVERED")));
        }

        @Test
        @DisplayName("GET /orders - Resposta deve incluir TOTAL calculado")
        void respostaDeveIncluirTotal() throws Exception {
                // Arrange
                Order order = new Order("Rua", -12.0, -38.0, Instant.now(), OrderStatus.PENDING);
                order.getProducts().add(pizza); // 45.90
                order.getProducts().add(refrigerante); // 8.00
                orderRepository.save(order);

                // Act & Assert
                mockMvc.perform(get("/orders"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].total", closeTo(53.90, 0.01)));
        }

        // Helpers
        private Order criarPedidoPendente() {
                Order order = new Order("Rua das Flores", -12.0, -38.0, Instant.now(), OrderStatus.PENDING);
                order.getProducts().add(pizza);
                return orderRepository.save(order);
        }

        private OrderDTO criarOrderDTO() {
                OrderDTO dto = new OrderDTO();
                dto.setAddress("Rua Nova, 456");
                dto.setLatitude(-13.0);
                dto.setLongitude(-39.0);
                dto.getProducts().add(new ProductDTO(pizza));
                return dto;
        }
}
