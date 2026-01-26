package com.portproject.delivery.controllers;

import com.portproject.delivery.entities.Product;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de QUALIDADE para ProductController
 * Inclui testes parametrizados com variação de volume
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIT {

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

        // ==================== TESTES BÁSICOS ====================

        @Test
        @DisplayName("GET /products - Deve retornar 404 quando não há produtos")
        void deveRetornar404_QuandoNaoHaProdutos() throws Exception {
                mockMvc.perform(get("/products"))
                                .andExpect(status().isNotFound());
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

        // ==================== TESTES PARAMETRIZADOS (MENOR, MÉDIO, MAIOR)
        // ====================

        @ParameterizedTest(name = "Listagem com {0} produtos")
        @ValueSource(ints = { 1, 10, 50 }) // MENOR, MÉDIO, MAIOR
        @DisplayName("GET /products - Deve retornar corretamente com diferentes QUANTIDADES")
        void deveRetornarCorretamente_ComDiferentesQuantidades(int qtdProdutos) throws Exception {
                // Arrange - criar N produtos
                for (int i = 1; i <= qtdProdutos; i++) {
                        productRepository.save(new Product(null, "Produto" + String.format("%03d", i),
                                        10.0 * i, "Descrição " + i, "uri" + i));
                }

                // Act & Assert
                mockMvc.perform(get("/products"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$", hasSize(qtdProdutos)));
        }

        @ParameterizedTest(name = "Ordenação com {0} produtos")
        @ValueSource(ints = { 3, 15, 30 }) // MENOR, MÉDIO, MAIOR
        @DisplayName("GET /products - Ordenação deve funcionar com diferentes QUANTIDADES")
        void ordenacaoDeveFuncionar_ComDiferentesQuantidades(int qtdProdutos) throws Exception {
                // Arrange - criar produtos com nomes em ordem inversa (Z, Y, X...)
                for (int i = qtdProdutos; i >= 1; i--) {
                        char letra = (char) ('A' + i - 1);
                        productRepository.save(new Product(null, letra + "_Produto", 10.0 * i, "Desc", "uri"));
                }

                // Act & Assert - primeiro deve ser 'A_Produto', último deve ser a última letra
                char primeiraLetra = 'A';
                char ultimaLetra = (char) ('A' + qtdProdutos - 1);

                mockMvc.perform(get("/products"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(qtdProdutos)))
                                .andExpect(jsonPath("$[0].name", startsWith(String.valueOf(primeiraLetra))))
                                .andExpect(jsonPath("$[" + (qtdProdutos - 1) + "].name",
                                                startsWith(String.valueOf(ultimaLetra))));
        }
}
