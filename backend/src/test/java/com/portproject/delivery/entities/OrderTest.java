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
        System.out.println("\n========================================");
        System.out.println("SETUP: Preparando dados para o teste");
        System.out.println("========================================");

        order = new Order("Rua das Flores, 123", -12.9714, -38.5014,
                Instant.parse("2025-01-15T14:30:00Z"), OrderStatus.PENDING);
        order.setId(1L);
        System.out.println("  - Order criado: ID=1, Status=PENDING");
        System.out.println("  - Endereco: Rua das Flores, 123");
        System.out.println("----------------------------------------\n");
    }

    // ==================== TESTES BÁSICOS ====================

    @Test
    @DisplayName("Total deve ser ZERO quando pedido não tem produtos")
    void total_DeveSerZero_QuandoSemProdutos() {
        System.out.println("TESTE: Total deve ser ZERO quando pedido nao tem produtos");
        System.out.println("----------------------------------------");

        System.out.println("[ARRANGE] Pedido sem produtos (lista vazia)");
        System.out.println("  - Quantidade de produtos: " + order.getProducts().size());

        System.out.println("[ACT] Calculando total...");
        Double total = order.getTotal();
        System.out.println("  - Total calculado: R$ " + total);

        System.out.println("[ASSERT] Verificando se total = 0.0...");
        assertEquals(0.0, order.getTotal(), "Pedido sem produtos = total zero");
        System.out.println("  - SUCESSO: Total e zero para pedido sem produtos");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Pedido NÃO deve aceitar produtos DUPLICADOS (Set)")
    void pedido_NaoDeveAceitarProdutosDuplicados() {
        System.out.println("TESTE: Pedido NAO deve aceitar produtos DUPLICADOS (Set)");
        System.out.println("----------------------------------------");

        System.out.println("[ARRANGE] Criando produto Pizza...");
        Product pizza = new Product(1L, "Pizza", 45.90, "Desc", "uri");
        System.out.println("  - Produto: Pizza (ID=1, R$ 45.90)");

        System.out.println("[ACT] Adicionando o MESMO produto 2 vezes...");
        order.getProducts().add(pizza);
        System.out.println("  - Primeira adicao: " + order.getProducts().size() + " produto(s)");
        order.getProducts().add(pizza);
        System.out.println("  - Segunda adicao: " + order.getProducts().size() + " produto(s)");

        System.out.println("[ASSERT] Verificando se tem apenas 1 produto...");
        assertEquals(1, order.getProducts().size(), "Set impede duplicados");
        System.out.println("  - SUCESSO: Set impediu duplicacao");
        System.out.println("========================================\n");
    }

    // ==================== TESTES PARAMETRIZADOS (MENOR, MÉDIO, MAIOR)
    // ====================

    @ParameterizedTest(name = "Cálculo de total com {0} produtos")
    @ValueSource(ints = { 1, 5, 20 }) // MENOR, MÉDIO, MAIOR
    @DisplayName("Total deve calcular corretamente com diferentes QUANTIDADES de produtos")
    void total_DeveCalcularCorretamente_ComDiferentesQuantidades(int qtdProdutos) {
        System.out.println("TESTE PARAMETRIZADO: Calculo total com " + qtdProdutos + " produtos");
        System.out.println("----------------------------------------");

        // Arrange - criar N produtos com preço 10.00 cada
        System.out.println("[ARRANGE] Criando " + qtdProdutos + " produtos (R$ 10.00 cada)...");
        double precoUnitario = 10.00;
        for (int i = 1; i <= qtdProdutos; i++) {
            Product produto = new Product((long) i, "Produto " + i, precoUnitario, "Desc", "uri");
            order.getProducts().add(produto);
        }
        System.out.println("  - Produtos criados: " + order.getProducts().size());

        // Act
        System.out.println("[ACT] Calculando total...");
        Double total = order.getTotal();
        System.out.println("  - Total calculado: R$ " + total);

        // Assert
        double totalEsperado = qtdProdutos * precoUnitario;
        System.out.println("[ASSERT] Verificando se total = R$ " + totalEsperado + "...");
        assertEquals(totalEsperado, total, 0.01,
                String.format("Total com %d produtos deve ser %.2f", qtdProdutos, totalEsperado));
        System.out.println("  - SUCESSO: Total calculado corretamente");
        System.out.println("========================================\n");
    }

    @ParameterizedTest(name = "Cálculo com preço {0} (valor {1})")
    @CsvSource({
            "0.01,   MENOR,  Centavos",
            "50.00,  MEDIO,  Normal",
            "9999.99, MAIOR, Alto"
    })
    @DisplayName("Total deve calcular corretamente com diferentes VALORES de preço")
    void total_DeveCalcularCorretamente_ComDiferentesValores(double preco, String categoria, String descricao) {
        System.out.println("TESTE PARAMETRIZADO: Categoria " + categoria + " - " + descricao);
        System.out.println("----------------------------------------");

        // Arrange
        System.out.println("[ARRANGE] Criando produto com preco: R$ " + preco);
        Product produto = new Product(1L, "Produto " + descricao, preco, "Desc", "uri");
        order.getProducts().add(produto);

        // Act
        System.out.println("[ACT] Calculando total...");
        Double total = order.getTotal();
        System.out.println("  - Total calculado: R$ " + total);

        // Assert
        System.out.println("[ASSERT] Verificando se total = R$ " + preco + "...");
        assertEquals(preco, total, 0.01,
                String.format("Categoria %s: preco %.2f deve ser calculado corretamente", categoria, preco));
        System.out.println("  - SUCESSO: Preco calculado corretamente para categoria " + categoria);
        System.out.println("========================================\n");
    }

    @ParameterizedTest(name = "Soma de {0} produtos com valores variados")
    @ValueSource(ints = { 2, 10, 50 }) // MENOR, MÉDIO, MAIOR
    @DisplayName("Total deve somar corretamente MÚLTIPLOS produtos com preços diferentes")
    void total_DeveSomarCorretamente_ComMultiplosProdutos(int qtdProdutos) {
        System.out.println("TESTE PARAMETRIZADO: Soma de " + qtdProdutos + " produtos variados");
        System.out.println("----------------------------------------");

        // Arrange - criar produtos com preços crescentes (10, 20, 30...)
        System.out.println("[ARRANGE] Criando produtos com precos crescentes (10, 20, 30...)");
        double somaEsperada = 0;
        for (int i = 1; i <= qtdProdutos; i++) {
            double preco = i * 10.0; // 10, 20, 30...
            Product produto = new Product((long) i, "Produto " + i, preco, "Desc", "uri");
            order.getProducts().add(produto);
            somaEsperada += preco;
        }
        System.out.println("  - Quantidade: " + qtdProdutos + " produtos");
        System.out.println("  - Soma esperada: R$ " + somaEsperada);

        // Act
        System.out.println("[ACT] Calculando total...");
        Double total = order.getTotal();
        System.out.println("  - Total calculado: R$ " + total);

        // Assert
        System.out.println("[ASSERT] Verificando se total = R$ " + somaEsperada + "...");
        assertEquals(somaEsperada, total, 0.01,
                String.format("Soma de %d produtos deve ser %.2f", qtdProdutos, somaEsperada));
        System.out.println("  - SUCESSO: Soma calculada corretamente");
        System.out.println("========================================\n");
    }
}
