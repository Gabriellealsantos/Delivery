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
        System.out.println("\n========================================");
        System.out.println("SETUP: Preparando dados para o teste");
        System.out.println("========================================");

        agua = new Product(1L, "Agua Mineral", 5.00, "500ml", "uri1");
        System.out.println("  - Produto criado: Agua Mineral (R$ 5.00)");

        hamburguer = new Product(2L, "Hamburguer", 35.00, "Artesanal", "uri2");
        System.out.println("  - Produto criado: Hamburguer (R$ 35.00)");

        pizza = new Product(3L, "Pizza", 45.90, "Margherita", "uri3");
        System.out.println("  - Produto criado: Pizza (R$ 45.90)");
        System.out.println("----------------------------------------\n");
    }

    @Test
    @DisplayName("REGRA: Produtos devem vir ORDENADOS por nome (A-Z)")
    void deveRetornarProdutosOrdenadosPorNome() {
        System.out.println("TESTE: Produtos devem vir ORDENADOS por nome (A-Z)");
        System.out.println("----------------------------------------");

        // Arrange
        System.out.println("[ARRANGE] Configurando mock para retornar produtos ordenados...");
        when(productRepository.findAllByOrderByNameAsc())
                .thenReturn(Arrays.asList(agua, hamburguer, pizza));
        System.out.println("  - Mock retornara: [Agua Mineral, Hamburguer, Pizza]");

        // Act
        System.out.println("[ACT] Chamando productService.findAllByOrderByNameAsc()...");
        List<ProductDTO> resultado = productService.findAllByOrderByNameAsc();
        System.out.println("  - Quantidade retornada: " + resultado.size() + " produtos");
        System.out.println("  - Ordem recebida:");
        for (int i = 0; i < resultado.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + resultado.get(i).getName());
        }

        // Assert
        System.out.println("[ASSERT] Verificando ordenacao A-Z...");
        assertEquals(3, resultado.size());
        assertEquals("Agua Mineral", resultado.get(0).getName());
        assertEquals("Hamburguer", resultado.get(1).getName());
        assertEquals("Pizza", resultado.get(2).getName());
        System.out.println("  - SUCESSO: Produtos ordenados corretamente A-Z");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Deve lancar EXCECAO quando nao ha produtos cadastrados")
    void deveLancarExcecao_QuandoNaoHaProdutos() {
        System.out.println("TESTE: Deve lancar EXCECAO quando nao ha produtos cadastrados");
        System.out.println("----------------------------------------");

        // Arrange
        System.out.println("[ARRANGE] Configurando mock para retornar lista vazia...");
        when(productRepository.findAllByOrderByNameAsc()).thenReturn(Collections.emptyList());
        System.out.println("  - Mock retornara: []");

        // Act & Assert
        System.out.println("[ACT/ASSERT] Chamando findAllByOrderByNameAsc e esperando excecao...");
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> productService.findAllByOrderByNameAsc());
        System.out.println("  - SUCESSO: Excecao ResourceNotFoundException lancada");
        System.out.println("  - Mensagem: " + exception.getMessage());
        System.out.println("========================================\n");
    }
}
