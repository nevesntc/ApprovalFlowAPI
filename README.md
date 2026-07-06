# Approval Flow API

API REST desenvolvida em Java com Spring Boot para gerenciar um fluxo corporativo de solicitações e aprovações.

O projeto simula um sistema interno onde uma pessoa cria uma solicitação, que pode ser aprovada ou reprovada. Ao mudar o status da solicitação, a aplicação publica eventos em uma fila RabbitMQ, demonstrando o uso de mensageria para desacoplar processos da regra principal.

## Objetivo do projeto

Este projeto foi criado para praticar e demonstrar conhecimentos em:

- Java
- Spring Boot
- Maven
- API REST
- Spring Data JPA
- DTOs
- Validações
- Tratamento global de exceções
- Testes unitários com JUnit e Mockito
- Swagger/OpenAPI
- RabbitMQ
- Docker Compose
- Arquitetura em camadas

## Tecnologias utilizadas

- Java 21
- Spring Boot 3.5.13
- Maven
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- SQL Server Driver
- Spring Boot Actuator
- Spring AMQP
- RabbitMQ
- Docker Compose
- JUnit 5
- Mockito
- AssertJ
- Swagger/OpenAPI

## Arquitetura

O projeto segue uma arquitetura em camadas:

```text
Controller → Service → Repository → Database
                ↓
             Mensageria
                ↓
             RabbitMQ