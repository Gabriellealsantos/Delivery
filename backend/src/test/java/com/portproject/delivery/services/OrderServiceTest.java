package com.portproject.delivery.services;

import com.portproject.delivery.dtos.OrderDTO;
import com.portproject.delivery.dtos.ProductDTO;
import com.portproject.delivery.entities.Order;
import com.portproject.delivery.entities.Product;
import com.portproject.delivery.entities.enums.OrderStatus;
import com.portproject.delivery.repositories.OrderRepository;
import com.portproject.delivery.repositories.ProductRepository;
import com.portproject.delivery.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes de QUALIDADE para OrderService
 * Total: 6 testes focados em regras de negócio críticas
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Product pizza;
    private Product refrigerante;
    private Order pedidoPendente;

    @BeforeEach
    void setUp() {
        pizza = new Product(1L, "Pizza", 45.90, "Margherita", "uri1");
        refrigerante = new Product(2L, "Refrigerante", 8.00, "Coca-Cola", "uri2");

        pedidoPendente = new Order("Rua das Flores, 123", -12.9714, -38.5014,
                Instant.parse("2025-01-15T10:00:00Z"), OrderStatus.PENDING);
        pedidoPendente.setId(1L);
        pedidoPendente.getProducts().add(pizza);
        pedidoPendente.getProducts().add(refrigerante);
    }

    // ==================== CRIAÇÃO DE PEDIDO ====================

    @Test
    @DisplayName("REGRA: Novo pedido SEMPRE deve ter status PENDING")
    void novoPedido_DeveTerStatusPending() {
        // Arrange
        OrderDTO dto = criarOrderDTO();
        configurarMocksParaInsert();

        // Act
        OrderDTO resultado = orderService.insert(dto);

        // Assert
        assertEquals(OrderStatus.PENDING, resultado.getStatus(),
                "REGRA DE NEGÓCIO: Todo pedido novo começa como PENDENTE");
    }

    @Test
    @DisplayName("REGRA: Momento do pedido deve ser definido AUTOMATICAMENTE")
    void novoPedido_DeveTerMomentoAutomatico() {
        // Arrange
        Instant antes = Instant.now();
        OrderDTO dto = criarOrderDTO();
        configurarMocksParaInsert();

        // Act
        OrderDTO resultado = orderService.insert(dto);
        Instant depois = Instant.now();

        // Assert
        assertNotNull(resultado.getMoment(), "Momento deve ser preenchido");
        assertTrue(!resultado.getMoment().isBefore(antes) && !resultado.getMoment().isAfter(depois),
                "Momento deve ser o instante atual");
    }

    @Test
    @DisplayName("REGRA: Produtos do DTO devem ser ASSOCIADOS ao pedido")
    void novoPedido_DeveAssociarProdutosDoDTO() {
        // Arrange
        OrderDTO dto = criarOrderDTO();
        dto.getProducts().add(new ProductDTO(refrigerante));

        when(productRepository.getReferenceById(1L)).thenReturn(pizza);
        when(productRepository.getReferenceById(2L)).thenReturn(refrigerante);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        when(orderRepository.save(captor.capture())).thenAnswer(inv -> {
            Order saved = inv.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        // Act
        orderService.insert(dto);

        // Assert
        Order pedidoSalvo = captor.getValue();
        assertEquals(2, pedidoSalvo.getProducts().size(), "Pedido deve ter os 2 produtos");
    }

    @Test
    @DisplayName("REGRA: Total do pedido deve ser CALCULADO corretamente")
    void novoPedido_DeveCalcularTotalCorretamente() {
        // Arrange
        OrderDTO dto = criarOrderDTO();
        dto.getProducts().add(new ProductDTO(refrigerante)); // +8.00

        when(productRepository.getReferenceById(1L)).thenReturn(pizza);
        when(productRepository.getReferenceById(2L)).thenReturn(refrigerante);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order saved = inv.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        // Act
        OrderDTO resultado = orderService.insert(dto);

        // Assert
        assertEquals(53.90, resultado.getTotal(), 0.01, "Total = 45.90 + 8.00");
    }

    // ==================== MARCAR COMO ENTREGUE ====================

    @Test
    @DisplayName("REGRA: Pedido PENDING deve mudar para DELIVERED")
    void pedidoPendente_DeveMudarParaDelivered() {
        // Arrange
        when(orderRepository.getReferenceById(1L)).thenReturn(pedidoPendente);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        OrderDTO resultado = orderService.setDelivered(1L);

        // Assert
        assertEquals(OrderStatus.DELIVERED, resultado.getStatus());
    }

    // ==================== LISTAGEM ====================

    @Test
    @DisplayName("Deve lançar EXCEÇÃO quando não há pedidos pendentes")
    void deveLancarExcecao_QuandoNaoHaPedidosPendentes() {
        // Arrange
        when(orderRepository.findOrdersWithProducts()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> orderService.findOrdersWithProducts());
    }

    // ==================== HELPERS ====================

    private OrderDTO criarOrderDTO() {
        OrderDTO dto = new OrderDTO();
        dto.setAddress("Rua Nova, 456");
        dto.setLatitude(-13.0);
        dto.setLongitude(-39.0);
        dto.getProducts().add(new ProductDTO(pizza)); // 45.90
        return dto;
    }

    private void configurarMocksParaInsert() {
        when(productRepository.getReferenceById(anyLong())).thenReturn(pizza);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order saved = inv.getArgument(0);
            saved.setId(10L);
            return saved;
        });
    }
}
