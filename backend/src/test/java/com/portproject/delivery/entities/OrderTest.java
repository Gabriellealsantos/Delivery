package com.portproject.delivery.entities;

import com.portproject.delivery.entities.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de QUALIDADE para Order
 * Inclui testes parametrizados com variação de volume (menor, médio, maior)
 */
class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order("Rua das Flores, 123", -12.9714, -38.5014,
                Instant.parse("2025-01-15T14:30:00Z"), OrderStatus.PENDING);
        order.setId(1L);
    }

    // ==================== TESTES BÁSICOS ====================

    @Test
    @DisplayName("Total deve ser ZERO quando pedido não tem produtos")
    void total_DeveSerZero_QuandoSemProdutos() {
        assertEquals(0.0, order.getTotal(), "Pedido sem produtos = total zero");
    }

    @Test
    @DisplayName("Pedido NÃO deve aceitar produtos DUPLICADOS (Set)")
    void pedido_NaoDeveAceitarProdutosDuplicados() {
        Product pizza = new Product(1L, "Pizza", 45.90, "Desc", "uri");
        order.getProducts().add(pizza);
        order.getProducts().add(pizza);
        assertEquals(1, order.getProducts().size(), "Set impede duplicados");
    }

    // ==================== TESTES PARAMETRIZADOS (MENOR, MÉDIO, MAIOR)
    // ====================

    @ParameterizedTest(name = "Cálculo de total com {0} produtos")
    @ValueSource(ints = { 1, 5, 20 }) // MENOR, MÉDIO, MAIOR
    @DisplayName("Total deve calcular corretamente com diferentes QUANTIDADES de produtos")
    void total_DeveCalcularCorretamente_ComDiferentesQuantidades(int qtdProdutos) {
        // Arrange - criar N produtos com preço 10.00 cada
        double precoUnitario = 10.00;
        for (int i = 1; i <= qtdProdutos; i++) {
            Product produto = new Product((long) i, "Produto " + i, precoUnitario, "Desc", "uri");
            order.getProducts().add(produto);
        }

        // Act
        Double total = order.getTotal();

        // Assert
        double totalEsperado = qtdProdutos * precoUnitario;
        assertEquals(totalEsperado, total, 0.01,
                String.format("Total com %d produtos deve ser %.2f", qtdProdutos, totalEsperado));
    }

    @ParameterizedTest(name = "Cálculo com preço {0} (valor {1})")
    @CsvSource({
            "0.01,   MENOR,  Centavos",
            "50.00,  MÉDIO,  Normal",
            "9999.99, MAIOR, Alto"
    })
    @DisplayName("Total deve calcular corretamente com diferentes VALORES de preço")
    void total_DeveCalcularCorretamente_ComDiferentesValores(double preco, String categoria, String descricao) {
        // Arrange
        Product produto = new Product(1L, "Produto " + descricao, preco, "Desc", "uri");
        order.getProducts().add(produto);

        // Act
        Double total = order.getTotal();

        // Assert
        assertEquals(preco, total, 0.01,
                String.format("Categoria %s: preço %.2f deve ser calculado corretamente", categoria, preco));
    }

    @ParameterizedTest(name = "Soma de {0} produtos com valores variados")
    @ValueSource(ints = { 2, 10, 50 }) // MENOR, MÉDIO, MAIOR
    @DisplayName("Total deve somar corretamente MÚLTIPLOS produtos com preços diferentes")
    void total_DeveSomarCorretamente_ComMultiplosProdutos(int qtdProdutos) {
        // Arrange - criar produtos com preços crescentes (10, 20, 30...)
        double somaEsperada = 0;
        for (int i = 1; i <= qtdProdutos; i++) {
            double preco = i * 10.0; // 10, 20, 30...
            Product produto = new Product((long) i, "Produto " + i, preco, "Desc", "uri");
            order.getProducts().add(produto);
            somaEsperada += preco;
        }

        // Act
        Double total = order.getTotal();

        // Assert
        assertEquals(somaEsperada, total, 0.01,
                String.format("Soma de %d produtos deve ser %.2f", qtdProdutos, somaEsperada));
    }
}
