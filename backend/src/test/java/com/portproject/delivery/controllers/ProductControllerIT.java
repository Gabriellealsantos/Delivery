package com.portproject.delivery.controllers;

import com.portproject.delivery.entities.Product;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de QUALIDADE para ProductController
 * Total: 3 testes de integração REST
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIT {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private OrderRepository orderRepository;

        @BeforeEach
        void setUp() {
                orderRepository.deleteAll();
                productRepository.deleteAll();
        }

        @Test
        @DisplayName("GET /products - Deve retornar 200 com lista de produtos")
        void deveRetornar200_ComProdutos() throws Exception {
                // Arrange
                productRepository.save(new Product(null, "Pizza", 45.90, "Desc", "uri"));
                productRepository.save(new Product(null, "Hamburguer", 35.00, "Desc", "uri"));

                // Act & Assert
                mockMvc.perform(get("/products"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        @DisplayName("GET /products - Produtos devem vir ORDENADOS alfabeticamente")
        void produtosDevemVirOrdenados() throws Exception {
                // Arrange (ordem NÃO alfabética)
                productRepository.save(new Product(null, "Pizza", 45.90, "Desc", "uri"));
                productRepository.save(new Product(null, "Agua", 5.00, "Desc", "uri"));
                productRepository.save(new Product(null, "Hamburguer", 35.00, "Desc", "uri"));

                // Act & Assert
                mockMvc.perform(get("/products"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name", is("Agua")))
                                .andExpect(jsonPath("$[1].name", is("Hamburguer")))
                                .andExpect(jsonPath("$[2].name", is("Pizza")));
        }

        @Test
        @DisplayName("GET /products - Deve retornar 404 quando não há produtos")
        void deveRetornar404_QuandoNaoHaProdutos() throws Exception {
                // Act & Assert
                mockMvc.perform(get("/products"))
                                .andExpect(status().isNotFound());
        }
}
