package com.portproject.delivery.services;

import com.portproject.delivery.dtos.ProductDTO;
import com.portproject.delivery.entities.Product;
import com.portproject.delivery.repositories.ProductRepository;
import com.portproject.delivery.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes de QUALIDADE para ProductService
 * Total: 2 testes focados em regras de negócio
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product agua;
    private Product hamburguer;
    private Product pizza;

    @BeforeEach
    void setUp() {
        agua = new Product(1L, "Água Mineral", 5.00, "500ml", "uri1");
        hamburguer = new Product(2L, "Hamburguer", 35.00, "Artesanal", "uri2");
        pizza = new Product(3L, "Pizza", 45.90, "Margherita", "uri3");
    }

    @Test
    @DisplayName("REGRA: Produtos devem vir ORDENADOS por nome (A-Z)")
    void deveRetornarProdutosOrdenadosPorNome() {
        // Arrange
        when(productRepository.findAllByOrderByNameAsc())
                .thenReturn(Arrays.asList(agua, hamburguer, pizza));

        // Act
        List<ProductDTO> resultado = productService.findAllByOrderByNameAsc();

        // Assert
        assertEquals(3, resultado.size());
        assertEquals("Água Mineral", resultado.get(0).getName());
        assertEquals("Hamburguer", resultado.get(1).getName());
        assertEquals("Pizza", resultado.get(2).getName());
    }

    @Test
    @DisplayName("Deve lançar EXCEÇÃO quando não há produtos cadastrados")
    void deveLancarExcecao_QuandoNaoHaProdutos() {
        // Arrange
        when(productRepository.findAllByOrderByNameAsc()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> productService.findAllByOrderByNameAsc());
    }
}
