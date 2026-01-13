package com.portproject.delivery.repositories;

import com.portproject.delivery.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de QUALIDADE para ProductRepository
 * Total: 1 teste da query de ordenação
 */
@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.getEntityManager()
                .createNativeQuery("DELETE FROM tb_order_product").executeUpdate();
        entityManager.getEntityManager()
                .createNativeQuery("DELETE FROM tb_order").executeUpdate();
        entityManager.getEntityManager()
                .createNativeQuery("DELETE FROM tb_product").executeUpdate();
    }

    @Test
    @DisplayName("REGRA: Produtos devem vir ORDENADOS alfabeticamente (A-Z)")
    void deveRetornarProdutosOrdenados() {
        // Arrange (ordem NÃO alfabética)
        entityManager.persist(new Product(null, "Pizza", 45.90, "Desc", "uri"));
        entityManager.persist(new Product(null, "Agua", 5.00, "Desc", "uri"));
        entityManager.persist(new Product(null, "Hamburguer", 35.00, "Desc", "uri"));
        entityManager.flush();

        // Act
        List<Product> resultado = productRepository.findAllByOrderByNameAsc();

        // Assert
        assertEquals(3, resultado.size());
        assertEquals("Agua", resultado.get(0).getName());
        assertEquals("Hamburguer", resultado.get(1).getName());
        assertEquals("Pizza", resultado.get(2).getName());
    }
}
