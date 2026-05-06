# ☕ Cafe Demand Forecast & AI Inventory Manager

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![AI/LLM](https://img.shields.io/badge/AI_Agent-000000?style=for-the-badge&logo=openai&logoColor=white)
![ClickUp](https://img.shields.io/badge/ClickUp-7B68EE?style=for-the-badge&logo=clickup&logoColor=white)

## 📌 Sobre o Projeto
O **Cafe Demand Forecast** é um sistema inteligente de gestão e previsão de estoque focado em operações de Food Service. Nascido de uma dor real na gestão operacional de uma cafeteria, o sistema monitora o inventário gerenciado no ClickUp e utiliza Inteligência Artificial para prever demandas e alertar sobre rupturas de estoque antes que elas aconteçam.

O projeto utiliza uma **Arquitetura de Microserviços**, onde o Core da aplicação (Autenticação, Banco de Dados e Regras de Negócio) é estruturado em Java, enquanto a inteligência de dados, LLMs e integrações analíticas são processadas por um serviço independente em Python.

## 🏗️ Arquitetura e Tecnologias

O sistema é dividido em dois serviços principais que se comunicam de forma assíncrona/REST:

1. **Core Service (Java / Spring Boot):**
   - Autenticação e Autorização via **JWT**.
   - Persistência e modelagem de dados operacionais com **PostgreSQL**.
   - Orquestração da regra de negócios da cafeteria.

2. **AI & Data Service (Python) - *[Em Desenvolvimento]*:**
   - Integração direta com a API do **ClickUp** para leitura em tempo real do estoque.
   - **Agente de Inteligência Artificial** com contexto do negócio para análise de consumo.
   - Algoritmo de previsão para identificar anomalias no consumo e disparar alertas de "Estoque Baixo" via mensageria/integração.

## 🚀 Status e Funcionalidades

### ✅ Implementado (Fase 1 - Core Backend)
- [x] Configuração e modelagem do banco de dados **PostgreSQL**.
- [x] Implementação de segurança corporativa utilizando tokens **JWT**.
- [x] CRUD básico para entidades do sistema (Usuários, Produtos, Categorias).

### 🚧 Próximos Passos (Fase 2 - Dados & IA)
- [ ] Integração do serviço Python com a API Rest do **ClickUp**.
- [ ] Construção do pipeline de extração de dados do estoque (ETL).
- [ ] Implementação de IA/LLM com prompt focado em contexto de Food Service.
- [ ] Configuração de comunicação entre o Microserviço Java e Python (via HTTP ou mensageria como RabbitMQ/Kafka).
- [ ] Disparo automatizado de alertas de ruptura de estoque.

## ⚙️ Como executar o projeto (Core Service)

### Pré-requisitos
- Java 11+
- PostgreSQL rodando localmente ou via Docker
- Maven

### Passos
1. Clone este repositório:
   ```bash
   git clone [https://github.com/BrenoDeJesuss/cafe-demand-forecast.git](https://github.com/BrenoDeJesuss/cafe-demand-forecast.git)
