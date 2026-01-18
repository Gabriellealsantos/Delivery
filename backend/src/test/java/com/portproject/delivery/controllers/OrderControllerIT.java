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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de QUALIDADE para OrderController
 * Inclui testes parametrizados com variação de volume
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

        private List<Product> produtos;

        @BeforeEach
        void setUp() {
                orderRepository.deleteAll();
                productRepository.deleteAll();

                // Criar vários produtos para os testes parametrizados
                produtos = new ArrayList<>();
                for (int i = 1; i <= 20; i++) {
                        Product p = productRepository.save(
                                        new Product(null, "Produto" + i, 10.0 * i, "Desc" + i, "uri" + i));
                        produtos.add(p);
                }
        }

        // ==================== TESTES BÁSICOS ====================

        @Test
        @DisplayName("GET /orders - Deve retornar 200 com pedidos PENDENTES")
        void deveRetornar200_ComPedidosPendentes() throws Exception {
                criarPedidoPendente(1);
                mockMvc.perform(get("/orders"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].status", is("PENDING")));
        }

        @Test
        @DisplayName("GET /orders - Deve retornar 404 quando não há pendentes")
        void deveRetornar404_QuandoNaoHaPendentes() throws Exception {
                Order entregue = new Order("Rua", -12.0, -38.0, Instant.now(), OrderStatus.DELIVERED);
                entregue.getProducts().add(produtos.get(0));
                orderRepository.save(entregue);

                mockMvc.perform(get("/orders"))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("POST /orders - Pedido criado deve ter status PENDING")
        void pedidoCriado_DeveTerStatusPending() throws Exception {
                OrderDTO dto = criarOrderDTO(1);

                mockMvc.perform(post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status", is("PENDING")));
        }

        @Test
        @DisplayName("PUT /orders/{id}/delivered - Deve mudar para DELIVERED")
        void deveRetornar200_ComStatusDelivered() throws Exception {
                Order pedido = criarPedidoPendente(1);

                mockMvc.perform(put("/orders/{id}/delivered", pedido.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status", is("DELIVERED")));
        }

        // ==================== TESTES PARAMETRIZADOS (MENOR, MÉDIO, MAIOR)
        // ====================

        @ParameterizedTest(name = "Pedido com {0} produtos")
        @ValueSource(ints = { 1, 5, 15 }) // MENOR, MÉDIO, MAIOR
        @DisplayName("POST /orders - Deve criar pedido com diferentes QUANTIDADES de produtos")
        void deveCriarPedido_ComDiferentesQuantidadesDeProdutos(int qtdProdutos) throws Exception {
                // Arrange
                OrderDTO dto = criarOrderDTO(qtdProdutos);

                // Act & Assert
                mockMvc.perform(post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status", is("PENDING")))
                                .andExpect(jsonPath("$.products", hasSize(qtdProdutos)));
        }

        @ParameterizedTest(name = "Total com {0} produtos")
        @ValueSource(ints = { 1, 5, 10 }) // MENOR, MÉDIO, MAIOR
        @DisplayName("GET /orders - Total deve ser calculado corretamente com diferentes QUANTIDADES")
        void totalDeveSerCalculado_ComDiferentesQuantidades(int qtdProdutos) throws Exception {
                // Arrange - criar pedido com N produtos
                Order order = new Order("Rua", -12.0, -38.0, Instant.now(), OrderStatus.PENDING);
                double totalEsperado = 0;
                for (int i = 0; i < qtdProdutos; i++) {
                        order.getProducts().add(produtos.get(i));
                        totalEsperado += produtos.get(i).getPrice();
                }
                orderRepository.save(order);

                // Act & Assert
                mockMvc.perform(get("/orders"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].total", closeTo(totalEsperado, 0.01)));
        }

        @ParameterizedTest(name = "Listagem de {0} pedidos pendentes")
        @ValueSource(ints = { 1, 5, 15 }) // MENOR, MÉDIO, MAIOR
        @DisplayName("GET /orders - Deve listar corretamente diferentes QUANTIDADES de pedidos")
        void deveListarCorretamente_ComDiferentesQuantidades(int qtdPedidos) throws Exception {
                // Arrange - criar N pedidos pendentes
                for (int i = 0; i < qtdPedidos; i++) {
                        criarPedidoPendente(1);
                }

                // Act & Assert
                mockMvc.perform(get("/orders"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(qtdPedidos)));
        }

        // ==================== HELPERS ====================

        private Order criarPedidoPendente(int qtdProdutos) {
                Order order = new Order("Rua das Flores", -12.0, -38.0, Instant.now(), OrderStatus.PENDING);
                for (int i = 0; i < qtdProdutos && i < produtos.size(); i++) {
                        order.getProducts().add(produtos.get(i));
                }
                return orderRepository.save(order);
        }

        private OrderDTO criarOrderDTO(int qtdProdutos) {
                OrderDTO dto = new OrderDTO();
                dto.setAddress("Rua Nova, 456");
                dto.setLatitude(-13.0);
                dto.setLongitude(-39.0);
                for (int i = 0; i < qtdProdutos && i < produtos.size(); i++) {
                        dto.getProducts().add(new ProductDTO(produtos.get(i)));
                }
                return dto;
        }
}
