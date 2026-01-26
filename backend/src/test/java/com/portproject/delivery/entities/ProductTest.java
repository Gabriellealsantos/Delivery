package com.portproject.delivery.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de QUALIDADE para Product
 * Total: 2 testes focados em validações
 */
public class ProductTest {

    @Test
    @DisplayName("ATENCAO: Produto ACEITA preco negativo (falta validacao!)")
    void produto_AceitaPrecoNegativo_FALTA_VALIDACAO() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Produto ACEITA preco negativo (FALTA VALIDACAO!)");
        System.out.println("========================================");

        System.out.println("[ARRANGE] Criando produto com preco NEGATIVO...");
        Product produto = new Product(1L, "Erro", -10.0, "Preco negativo", "uri");
        System.out.println("  - Produto: Erro");
        System.out.println("  - Preco definido: R$ -10.00");

        System.out.println("[ACT] Obtendo preco do produto...");
        Double preco = produto.getPrice();
        System.out.println("  - Preco retornado: R$ " + preco);

        System.out.println("[ASSERT] Verificando se sistema aceitou preco negativo...");
        assertEquals(-10.0, produto.getPrice(),
                "BUG: Sistema aceita preco negativo - implementar validacao!");
        System.out.println("  - ATENCAO: Sistema aceitou preco negativo!");
        System.out.println("  - TODO: Implementar validacao para impedir precos negativos");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Produtos com mesmo ID sao IGUAIS (para uso em Set)")
    void produtosComMesmoId_SaoIguais() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Produtos com mesmo ID sao IGUAIS (para uso em Set)");
        System.out.println("========================================");

        // Arrange
        System.out.println("[ARRANGE] Criando 2 produtos com MESMO ID mas dados diferentes...");
        Product pizza1 = new Product(1L, "Pizza A", 40.0, "Desc", "uri");
        System.out.println("  - Pizza1: ID=1, Nome='Pizza A', Preco=R$ 40.00");

        Product pizza2 = new Product(1L, "Pizza B", 50.0, "Outra", "uri2");
        System.out.println("  - Pizza2: ID=1, Nome='Pizza B', Preco=R$ 50.00");

        // Act
        System.out.println("[ACT] Comparando produtos...");
        boolean saoIguais = pizza1.equals(pizza2);
        boolean hashCodeIgual = pizza1.hashCode() == pizza2.hashCode();
        System.out.println("  - pizza1.equals(pizza2): " + saoIguais);
        System.out.println("  - hashCode iguais: " + hashCodeIgual);

        // Assert
        System.out.println("[ASSERT] Verificando igualdade por ID...");
        assertEquals(pizza1, pizza2, "Mesmo ID = mesmo produto (para Set)");
        assertEquals(pizza1.hashCode(), pizza2.hashCode());
        System.out.println("  - SUCESSO: Produtos com mesmo ID sao considerados iguais");
        System.out.println("  - Isso permite uso correto em Set (evita duplicados por ID)");
        System.out.println("========================================\n");
    }
}
