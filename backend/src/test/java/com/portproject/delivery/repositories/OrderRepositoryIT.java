package com.portproject.delivery.repositories;

import com.portproject.delivery.entities.Order;
import com.portproject.delivery.entities.Product;
import com.portproject.delivery.entities.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de QUALIDADE para OrderRepository
 * Total: 2 testes da query JPQL customizada
 */
@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryIT {

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private TestEntityManager entityManager;

        private Product pizza;

        @BeforeEach
        void setUp() {
                entityManager.getEntityManager()
                                .createNativeQuery("DELETE FROM tb_order_product").executeUpdate();
                entityManager.getEntityManager()
                                .createNativeQuery("DELETE FROM tb_order").executeUpdate();
                entityManager.getEntityManager()
                                .createNativeQuery("DELETE FROM tb_product").executeUpdate();

                pizza = entityManager.persist(new Product(null, "Pizza", 45.90, "Desc", "uri"));
                entityManager.flush();
        }

        @Test
        @DisplayName("REGRA: Query deve retornar APENAS pedidos PENDING (status=0)")
        void deveRetornarApenasPedidosPending() {
                // Arrange
                Order pendente = criarPedido(OrderStatus.PENDING);
                Order entregue = criarPedido(OrderStatus.DELIVERED);
                entityManager.persist(pendente);
                entityManager.persist(entregue);
                entityManager.flush();

                // Act
                List<Order> resultado = orderRepository.findOrdersWithProducts();

                // Assert
                assertEquals(1, resultado.size());
                assertEquals(OrderStatus.PENDING, resultado.get(0).getStatus());
        }

        @Test
        @DisplayName("REGRA: Query deve trazer PRODUTOS (JOIN FETCH)")
        void deveTrazerProdutosJuntoComPedido() {
                // Arrange
                Order pedido = criarPedido(OrderStatus.PENDING);
                entityManager.persist(pedido);
                entityManager.flush();
                entityManager.clear(); // Limpa cache

                // Act
                List<Order> resultado = orderRepository.findOrdersWithProducts();

                // Assert
                assertFalse(resultado.isEmpty());
                assertEquals(1, resultado.get(0).getProducts().size(),
                                "JOIN FETCH deve trazer produtos");
        }

        private Order criarPedido(OrderStatus status) {
                Order order = new Order("Rua", -12.0, -38.0, Instant.now(), status);
                order.getProducts().add(pizza);
                return order;
        }
}
