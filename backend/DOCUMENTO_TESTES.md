# Documento de Casos de Teste - Fast Delivery Backend

## Resumo

- **Total de Testes**: 45
- **Testes Unitários**: 19
- **Testes de Integração**: 26
- **Status**: ✅ Todos passando

---

## 1. OrderServiceTest (6 testes unitários)

### 1.1 `novoPedido_DeveTerStatusPending`

- **O que testa**: Novo pedido recebe status PENDING automaticamente
- **Por quê**: Regra de negócio - pedidos sempre começam pendentes
- **Volume**: Único cenário

### 1.2 `novoPedido_DeveTerMomentoAutomatico`

- **O que testa**: Campo `moment` é preenchido com `Instant.now()`
- **Por quê**: Timestamp deve ser automático, não informado pelo usuário

### 1.3 `novoPedido_DeveAssociarProdutosDoDTO`

- **O que testa**: Produtos do DTO são associados ao pedido
- **Por quê**: Sem associação, pedido não teria itens

### 1.4 `novoPedido_DeveCalcularTotalCorretamente`

- **O que testa**: Total = soma dos preços (45.90 + 8.00 = 53.90)
- **Por quê**: Cálculo financeiro deve estar correto

### 1.5 `pedidoPendente_DeveMudarParaDelivered`

- **O que testa**: `setDelivered()` altera status para DELIVERED
- **Por quê**: Fluxo principal do sistema

### 1.6 `deveLancarExcecao_QuandoNaoHaPedidosPendentes`

- **O que testa**: Lança `ResourceNotFoundException` quando lista vazia
- **Por quê**: Tratamento adequado de erro

---

## 2. ProductServiceTest (2 testes unitários)

### 2.1 `deveRetornarProdutosOrdenadosPorNome`

- **O que testa**: Produtos ordenados alfabeticamente
- **Por quê**: Exibição organizada no cardápio

### 2.2 `deveLancarExcecao_QuandoNaoHaProdutos`

- **O que testa**: Exceção quando não há produtos
- **Por quê**: Tratamento de cardápio vazio

---

## 3. OrderTest (5 testes unitários + 6 parametrizados = 11 testes)

### Testes Básicos

### 3.1 `total_DeveSerZero_QuandoSemProdutos`

- **O que testa**: `getTotal()` = 0 sem produtos
- **Por quê**: Edge case

### 3.2 `pedido_NaoDeveAceitarProdutosDuplicados`

- **O que testa**: Set impede duplicados
- **Por quê**: Integridade de dados

### Testes Parametrizados (Menor, Médio, Maior)

### 3.3 `total_DeveCalcularCorretamente_ComDiferentesQuantidades`

| Volume | Qtd Produtos | Total Esperado |
| ------ | ------------ | -------------- |
| MENOR  | 1            | R$ 10,00       |
| MÉDIO  | 5            | R$ 50,00       |
| MAIOR  | 20           | R$ 200,00      |

- **Por quê**: Verificar se cálculo funciona com diferentes volumes

### 3.4 `total_DeveCalcularCorretamente_ComDiferentesValores`

| Volume | Preço       | Descrição  |
| ------ | ----------- | ---------- |
| MENOR  | R$ 0,01     | Centavos   |
| MÉDIO  | R$ 50,00    | Normal     |
| MAIOR  | R$ 9.999,99 | Valor alto |

- **Por quê**: Testar limites de valores

### 3.5 `total_DeveSomarCorretamente_ComMultiplosProdutos`

| Volume | Qtd Produtos | Soma (10+20+30...) |
| ------ | ------------ | ------------------ |
| MENOR  | 2            | R$ 30,00           |
| MÉDIO  | 10           | R$ 550,00          |
| MAIOR  | 50           | R$ 12.750,00       |

- **Por quê**: Soma com muitos itens

---

## 4. ProductTest (2 testes unitários)

### 4.1 `produto_AceitaPrecoNegativo_FALTA_VALIDACAO`

- **O que testa**: Sistema aceita preço negativo (bug)
- **Por quê**: Documenta necessidade de validação

### 4.2 `produtosComMesmoId_SaoIguais`

- **O que testa**: Equals por ID
- **Por quê**: Necessário para Set

---

## 5. OrderControllerIT (13 testes de integração)

### Testes Básicos

### 5.1 `deveRetornar200_ComPedidosPendentes`

- **O que testa**: GET /orders retorna 200

### 5.2 `deveRetornar404_QuandoNaoHaPendentes`

- **O que testa**: GET /orders retorna 404 quando só há entregues

### 5.3 `pedidoCriado_DeveTerStatusPending`

- **O que testa**: POST /orders cria com status PENDING

### 5.4 `deveRetornar200_ComStatusDelivered`

- **O que testa**: PUT /orders/{id}/delivered muda status

### Testes Parametrizados (Menor, Médio, Maior)

### 5.5 `deveCriarPedido_ComDiferentesQuantidadesDeProdutos`

| Volume | Qtd Produtos |
| ------ | ------------ |
| MENOR  | 1            |
| MÉDIO  | 5            |
| MAIOR  | 15           |

- **Por quê**: Testar criação com diferentes volumes

### 5.6 `totalDeveSerCalculado_ComDiferentesQuantidades`

| Volume | Qtd Produtos | Total     |
| ------ | ------------ | --------- |
| MENOR  | 1            | R$ 10,00  |
| MÉDIO  | 5            | R$ 150,00 |
| MAIOR  | 10           | R$ 550,00 |

- **Por quê**: Verificar cálculo de total via API

### 5.7 `deveListarCorretamente_ComDiferentesQuantidades`

| Volume | Qtd Pedidos |
| ------ | ----------- |
| MENOR  | 1           |
| MÉDIO  | 5           |
| MAIOR  | 15          |

- **Por quê**: Listagem com diferentes volumes

---

## 6. ProductControllerIT (8 testes de integração)

### Testes Básicos

### 6.1 `deveRetornar404_QuandoNaoHaProdutos`

- **O que testa**: GET /products retorna 404

### 6.2 `produtosDevemVirOrdenados`

- **O que testa**: Ordenação alfabética

### Testes Parametrizados (Menor, Médio, Maior)

### 6.3 `deveRetornarCorretamente_ComDiferentesQuantidades`

| Volume | Qtd Produtos |
| ------ | ------------ |
| MENOR  | 1            |
| MÉDIO  | 10           |
| MAIOR  | 50           |

- **Por quê**: Listagem com diferentes volumes

### 6.4 `ordenacaoDeveFuncionar_ComDiferentesQuantidades`

| Volume | Qtd Produtos | Primeiro | Último |
| ------ | ------------ | -------- | ------ |
| MENOR  | 3            | A_...    | C_...  |
| MÉDIO  | 15           | A_...    | O_...  |
| MAIOR  | 30           | A_...    | ^_...  |

- **Por quê**: Ordenação funciona com muitos itens

---

## 7. OrderRepositoryIT (2 testes de integração)

### 7.1 `deveRetornarApenasPedidosPending`

- **O que testa**: Query filtra status=0

### 7.2 `deveTrazerProdutosJuntoComPedido`

- **O que testa**: JOIN FETCH funciona

---

## 8. ProductRepositoryIT (1 teste de integração)

### 8.1 `deveRetornarProdutosOrdenados`

- **O que testa**: Query ordena por nome

---

## Resumo dos Testes Parametrizados

| Cenário              | MENOR | MÉDIO | MAIOR |
| -------------------- | ----- | ----- | ----- |
| Produtos por pedido  | 1     | 5     | 15-20 |
| Valor do produto     | 0.01  | 50.00 | 9999  |
| Produtos na listagem | 1     | 10    | 50    |
| Pedidos pendentes    | 1     | 5     | 15    |

---

## Tecnologias

- JUnit 5 + `@ParameterizedTest`
- Mockito
- Spring Boot Test
- MockMvc
- H2 Database

## Como Executar

```bash
./mvnw test
```
