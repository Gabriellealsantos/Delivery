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
        System.out.println("\n========================================");
        System.out.println("SETUP: Preparando dados para o teste");
        System.out.println("========================================");

        pizza = new Product(1L, "Pizza", 45.90, "Margherita", "uri1");
        System.out.println("  - Produto criado: Pizza (R$ 45.90)");

        refrigerante = new Product(2L, "Refrigerante", 8.00, "Coca-Cola", "uri2");
        System.out.println("  - Produto criado: Refrigerante (R$ 8.00)");

        pedidoPendente = new Order("Rua das Flores, 123", -12.9714, -38.5014,
                Instant.parse("2025-01-15T10:00:00Z"), OrderStatus.PENDING);
        pedidoPendente.setId(1L);
        pedidoPendente.getProducts().add(pizza);
        pedidoPendente.getProducts().add(refrigerante);
        System.out.println("  - Pedido pendente criado: ID=1, Status=PENDING");
        System.out.println("  - Produtos no pedido: Pizza + Refrigerante");
        System.out.println("----------------------------------------\n");
    }

    // ==================== CRIAÇÃO DE PEDIDO ====================

    @Test
    @DisplayName("REGRA: Novo pedido SEMPRE deve ter status PENDING")
    void novoPedido_DeveTerStatusPending() {
        System.out.println("TESTE: Novo pedido SEMPRE deve ter status PENDING");
        System.out.println("----------------------------------------");

        // Arrange
        System.out.println("[ARRANGE] Criando DTO do pedido...");
        OrderDTO dto = criarOrderDTO();
        System.out.println("  - DTO criado com endereco: " + dto.getAddress());

        System.out.println("[ARRANGE] Configurando mocks...");
        configurarMocksParaInsert();
        System.out.println("  - Mocks configurados para insert");

        // Act
        System.out.println("[ACT] Chamando orderService.insert(dto)...");
        OrderDTO resultado = orderService.insert(dto);
        System.out.println("  - Pedido inserido com ID: " + resultado.getId());
        System.out.println("  - Status retornado: " + resultado.getStatus());

        // Assert
        System.out.println("[ASSERT] Verificando se status = PENDING...");
        assertEquals(OrderStatus.PENDING, resultado.getStatus(),
                "REGRA DE NEGOCIO: Todo pedido novo comeca como PENDENTE");
        System.out.println("  - SUCESSO: Status confirmado como PENDING");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("REGRA: Momento do pedido deve ser definido AUTOMATICAMENTE")
    void novoPedido_DeveTerMomentoAutomatico() {
        System.out.println("TESTE: Momento do pedido deve ser definido AUTOMATICAMENTE");
        System.out.println("----------------------------------------");

        // Arrange
        System.out.println("[ARRANGE] Capturando instante ANTES da criacao...");
        Instant antes = Instant.now();
        System.out.println("  - Instante antes: " + antes);

        OrderDTO dto = criarOrderDTO();
        configurarMocksParaInsert();
        System.out.println("  - DTO e mocks configurados");

        // Act
        System.out.println("[ACT] Chamando orderService.insert(dto)...");
        OrderDTO resultado = orderService.insert(dto);
        Instant depois = Instant.now();
        System.out.println("  - Instante depois: " + depois);
        System.out.println("  - Momento do pedido: " + resultado.getMoment());

        // Assert
        System.out.println("[ASSERT] Verificando se momento esta entre 'antes' e 'depois'...");
        assertNotNull(resultado.getMoment(), "Momento deve ser preenchido");
        assertTrue(!resultado.getMoment().isBefore(antes) && !resultado.getMoment().isAfter(depois),
                "Momento deve ser o instante atual");
        System.out.println("  - SUCESSO: Momento definido automaticamente");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("REGRA: Produtos do DTO devem ser ASSOCIADOS ao pedido")
    void novoPedido_DeveAssociarProdutosDoDTO() {
        System.out.println("TESTE: Produtos do DTO devem ser ASSOCIADOS ao pedido");
        System.out.println("----------------------------------------");

        // Arrange
        System.out.println("[ARRANGE] Criando DTO com 2 produtos...");
        OrderDTO dto = criarOrderDTO();
        dto.getProducts().add(new ProductDTO(refrigerante));
        System.out.println("  - Produtos no DTO: Pizza + Refrigerante");

        System.out.println("[ARRANGE] Configurando mocks para retornar produtos...");
        when(productRepository.getReferenceById(1L)).thenReturn(pizza);
        when(productRepository.getReferenceById(2L)).thenReturn(refrigerante);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        when(orderRepository.save(captor.capture())).thenAnswer(inv -> {
            Order saved = inv.getArgument(0);
            saved.setId(10L);
            return saved;
        });
        System.out.println("  - Mocks configurados com ArgumentCaptor");

        // Act
        System.out.println("[ACT] Chamando orderService.insert(dto)...");
        orderService.insert(dto);
        System.out.println("  - Pedido inserido");

        // Assert
        System.out.println("[ASSERT] Verificando produtos associados...");
        Order pedidoSalvo = captor.getValue();
        System.out.println("  - Quantidade de produtos no pedido salvo: " + pedidoSalvo.getProducts().size());
        assertEquals(2, pedidoSalvo.getProducts().size(), "Pedido deve ter os 2 produtos");
        System.out.println("  - SUCESSO: 2 produtos associados corretamente");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("REGRA: Total do pedido deve ser CALCULADO corretamente")
    void novoPedido_DeveCalcularTotalCorretamente() {
        System.out.println("TESTE: Total do pedido deve ser CALCULADO corretamente");
        System.out.println("----------------------------------------");

        // Arrange
        System.out.println("[ARRANGE] Criando DTO com Pizza + Refrigerante...");
        OrderDTO dto = criarOrderDTO();
        dto.getProducts().add(new ProductDTO(refrigerante)); // +8.00
        System.out.println("  - Pizza: R$ 45.90");
        System.out.println("  - Refrigerante: R$ 8.00");
        System.out.println("  - Total esperado: R$ 53.90");

        System.out.println("[ARRANGE] Configurando mocks...");
        when(productRepository.getReferenceById(1L)).thenReturn(pizza);
        when(productRepository.getReferenceById(2L)).thenReturn(refrigerante);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order saved = inv.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        // Act
        System.out.println("[ACT] Chamando orderService.insert(dto)...");
        OrderDTO resultado = orderService.insert(dto);
        System.out.println("  - Total calculado: R$ " + resultado.getTotal());

        // Assert
        System.out.println("[ASSERT] Verificando se total = R$ 53.90...");
        assertEquals(53.90, resultado.getTotal(), 0.01, "Total = 45.90 + 8.00");
        System.out.println("  - SUCESSO: Total calculado corretamente");
        System.out.println("========================================\n");
    }

    // ==================== MARCAR COMO ENTREGUE ====================

    @Test
    @DisplayName("REGRA: Pedido PENDING deve mudar para DELIVERED")
    void pedidoPendente_DeveMudarParaDelivered() {
        System.out.println("TESTE: Pedido PENDING deve mudar para DELIVERED");
        System.out.println("----------------------------------------");

        // Arrange
        System.out.println("[ARRANGE] Configurando mock para retornar pedido pendente...");
        System.out.println("  - Pedido ID: 1, Status atual: PENDING");
        when(orderRepository.getReferenceById(1L)).thenReturn(pedidoPendente);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        System.out.println("[ACT] Chamando orderService.setDelivered(1L)...");
        OrderDTO resultado = orderService.setDelivered(1L);
        System.out.println("  - Status apos operacao: " + resultado.getStatus());

        // Assert
        System.out.println("[ASSERT] Verificando se status = DELIVERED...");
        assertEquals(OrderStatus.DELIVERED, resultado.getStatus());
        System.out.println("  - SUCESSO: Status alterado para DELIVERED");
        System.out.println("========================================\n");
    }

    // ==================== LISTAGEM ====================

    @Test
    @DisplayName("Deve lançar EXCEÇÃO quando não há pedidos pendentes")
    void deveLancarExcecao_QuandoNaoHaPedidosPendentes() {
        System.out.println("TESTE: Deve lancar EXCECAO quando nao ha pedidos pendentes");
        System.out.println("----------------------------------------");

        // Arrange
        System.out.println("[ARRANGE] Configurando mock para retornar lista vazia...");
        when(orderRepository.findOrdersWithProducts()).thenReturn(Collections.emptyList());
        System.out.println("  - Repository retornara: []");

        // Act & Assert
        System.out.println("[ACT/ASSERT] Chamando findOrdersWithProducts e esperando excecao...");
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.findOrdersWithProducts());
        System.out.println("  - SUCESSO: Excecao ResourceNotFoundException lancada");
        System.out.println("  - Mensagem: " + exception.getMessage());
        System.out.println("========================================\n");
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
