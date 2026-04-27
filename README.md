# ☕ Café Demand Forecast

Sistema inteligente de **previsão de demanda e sugestão de produção** para cafeteria. Utiliza Inteligência Artificial (Groq API) para analisar o histórico de vendas e gerar recomendações de produção diária.

---

## 🚀 Funcionalidades

| Funcionalidade | Status |
|---|---|
| CRUD de Produtos | ✅ Implementado |
| Registro de vendas diárias | ✅ Implementado |
| Consulta por período e dia da semana | ✅ Implementado |
| Previsão de demanda com IA (Groq) | ✅ Implementado |
| Sugestão de produção com IA | ✅ Implementado |
| DTOs com MapStruct | ✅ Implementado |
| Documentação Swagger/OpenAPI | ✅ Implementado |
| Autenticação JWT | 🔜 Planejado |
| Migração para PostgreSQL | 🔜 Planejado |
| Frontend React | 🔜 Planejado |

---

## 🛠️ Stack

| Tecnologia | Uso |
|---|---|
| Java 17 + Spring Boot 4 | Framework principal |
| Spring Data JPA | Persistência |
| Spring Security | Segurança (JWT planejado) |
| H2 Database | Banco em memória (dev) |
| Groq API (llama-3.3-70b) | IA para previsão de demanda |
| MapStruct | Mapeamento DTO ↔ Entity |
| Springdoc OpenAPI 2.8.8 | Documentação Swagger |
| Lombok | Redução de boilerplate |
| Maven | Gerenciamento de dependências |

---

## ⚙️ Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.9+
- Conta gratuita no [Groq Console](https://console.groq.com)

### Configuração

1. Clone o repositório:

```bash
git clone https://github.com/butyrum/cafe-demand-forecast.git
cd cafe-demand-forecast
```

2. Configure a variável de ambiente com sua chave Groq:

```bash
# Windows
set GROQ_API_KEY=sua_chave_aqui

# Linux/Mac
export GROQ_API_KEY=sua_chave_aqui
```

3. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

### Acessos

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| Console H2 | http://localhost:8080/h2-console |

**Configuração H2 Console:**
- JDBC URL: `jdbc:h2:mem:cafe_forecast`
- Username: `sa`
- Password: *(vazio)*

---

## 📡 Endpoints da API

### Produtos `/api/products`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/products` | Listar todos os produtos |
| GET | `/api/products/{id}` | Buscar produto por ID |
| GET | `/api/products/active` | Listar produtos ativos |
| GET | `/api/products/category/{category}` | Buscar por categoria |
| POST | `/api/products` | Criar novo produto |
| PUT | `/api/products/{id}` | Atualizar produto |
| DELETE | `/api/products/{id}` | Desativar produto |

### Vendas `/api/sales`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/sales` | Listar todos os registros |
| GET | `/api/sales/{id}` | Buscar por ID |
| GET | `/api/sales/product/{productId}` | Vendas por produto |
| GET | `/api/sales/period?startDate=&endDate=` | Vendas por período |
| GET | `/api/sales/day/{dayOfWeek}` | Vendas por dia da semana |
| GET | `/api/sales/product/{productId}/total` | Total vendido por produto |
| POST | `/api/sales` | Registrar nova venda |
| DELETE | `/api/sales/{id}` | Deletar registro |

### Previsão de Demanda com IA `/api/forecast`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/forecast/demand/{productId}?productName=` | Previsão baseada no histórico |
| POST | `/api/forecast/production` | Sugestão de quantidade a produzir |

**Exemplo — Sugestão de produção:**

```json
POST /api/forecast/production
{
  "productName": "Cappuccino",
  "forecastedDemand": 20,
  "currentStock": 5
}
```

---

## 📁 Arquitetura

```
src/main/java/com/cafe/forecast/
├── config/          # SecurityConfig, SwaggerConfig
├── controller/      # ProductController, SalesRecordController, ForecastController
├── dto/             # ProductDTO, SalesRecordDTO
├── mapper/          # ProductMapper, SalesRecordMapper (MapStruct)
├── model/           # Product, SalesRecord (entidades JPA)
├── repository/      # ProductRepository, SalesRecordRepository
└── service/         # ProductService, SalesRecordService, AIForecastService
```

---

## 🗺️ Roadmap

- [x] Fase 1 — CRUD de Produtos e Registros de Vendas
- [x] Fase 2 — Integração com IA (Groq API)
- [x] Fase 3 — DTOs com MapStruct
- [ ] Fase 4 — Autenticação JWT com Spring Security
- [ ] Fase 5 — Migração para PostgreSQL
- [ ] Fase 6 — Módulo de Estoque e Ingredientes
- [ ] Fase 7 — Frontend em React
- [ ] Fase 8 — Integração com PDV (Mogo) e iFood

---

## 👨‍💻 Autor

**Breno de Jesus** — [@butyrum](https://github.com/butyrum)

Projeto desenvolvido como freelance para gestão inteligente de cafeterias.
