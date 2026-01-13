package com.portproject.delivery.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de QUALIDADE para Product
 * Total: 2 testes focados em validações
 */
class ProductTest {

    @Test
    @DisplayName("⚠️ ATENÇÃO: Produto ACEITA preço negativo (falta validação!)")
    void produto_AceitaPrecoNegativo_FALTA_VALIDACAO() {
        // Este teste DOCUMENTA um bug/falta de validação
        Product produto = new Product(1L, "Erro", -10.0, "Preço negativo", "uri");

        assertEquals(-10.0, produto.getPrice(),
                "BUG: Sistema aceita preço negativo - implementar validação!");
    }

    @Test
    @DisplayName("Produtos com mesmo ID são IGUAIS (para uso em Set)")
    void produtosComMesmoId_SaoIguais() {
        // Arrange
        Product pizza1 = new Product(1L, "Pizza A", 40.0, "Desc", "uri");
        Product pizza2 = new Product(1L, "Pizza B", 50.0, "Outra", "uri2");

        // Assert
        assertEquals(pizza1, pizza2, "Mesmo ID = mesmo produto (para Set)");
        assertEquals(pizza1.hashCode(), pizza2.hashCode());
    }
}
