package com.portproject.delivery.entities;

import com.portproject.delivery.entities.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de QUALIDADE para Order
 * Total: 3 testes focados em regras de negócio
 */
class OrderTest {

    private Order order;
    private Product pizza;
    private Product refrigerante;

    @BeforeEach
    void setUp() {
        order = new Order("Rua das Flores, 123", -12.9714, -38.5014,
                Instant.parse("2025-01-15T14:30:00Z"), OrderStatus.PENDING);
        order.setId(1L);

        pizza = new Product(1L, "Pizza", 45.90, "Pizza margherita", "uri1");
        refrigerante = new Product(2L, "Refrigerante", 8.00, "Coca-Cola", "uri2");
    }

    @Test
    @DisplayName("Total deve ser ZERO quando pedido não tem produtos")
    void total_DeveSerZero_QuandoSemProdutos() {
        assertEquals(0.0, order.getTotal(), "Pedido sem produtos = total zero");
    }

    @Test
    @DisplayName("Total deve SOMAR preços de todos os produtos")
    void total_DeveSomarPrecos_DeTodosOsProdutos() {
        // Arrange
        order.getProducts().add(pizza); // 45.90
        order.getProducts().add(refrigerante); // 8.00

        // Act & Assert
        assertEquals(53.90, order.getTotal(), 0.01, "Total = soma dos preços");
    }

    @Test
    @DisplayName("Pedido NÃO deve aceitar produtos DUPLICADOS (Set)")
    void pedido_NaoDeveAceitarProdutosDuplicados() {
        // Act
        order.getProducts().add(pizza);
        order.getProducts().add(pizza); // tentativa de duplicar

        // Assert
        assertEquals(1, order.getProducts().size(), "Set impede duplicados");
    }
}
