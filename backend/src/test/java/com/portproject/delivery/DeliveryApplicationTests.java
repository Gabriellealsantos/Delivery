package com.portproject.delivery;

import com.portproject.delivery.controllers.OrderControllerIT;
import com.portproject.delivery.controllers.ProductControllerIT;
import com.portproject.delivery.entities.OrderTest;
import com.portproject.delivery.entities.ProductTest;
import com.portproject.delivery.repositories.OrderRepositoryIT;
import com.portproject.delivery.repositories.ProductRepositoryIT;
import com.portproject.delivery.services.OrderServiceTest;
import com.portproject.delivery.services.ProductServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test Suite que roda TODOS os testes do projeto.
 * 
 * Inclui:
 * - Testes unitarios (entities, services)
 * - Testes de integracao (controllers, repositories)
 * 
 * Para executar: clique com botao direito nesta classe e selecione "Run"
 */
@Suite
@SelectClasses({
        // Testes de Entidades
        OrderTest.class,
        ProductTest.class,

        // Testes de Services (unitarios com Mockito)
        OrderServiceTest.class,
        ProductServiceTest.class,

        // Testes de Repositories (integracao com banco)
        OrderRepositoryIT.class,
        ProductRepositoryIT.class,

        // Testes de Controllers (integracao com MockMvc)
        OrderControllerIT.class,
        ProductControllerIT.class
})
class DeliveryApplicationTests {
}