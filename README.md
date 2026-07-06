# Approval Flow API

API REST desenvolvida em Java com Spring Boot para gerenciar um fluxo corporativo de solicitações e aprovações.

O projeto simula um sistema interno onde uma pessoa cria uma solicitação, que pode ser aprovada ou reprovada. Ao mudar o status da solicitação, a aplicação publica eventos em filas RabbitMQ, demonstrando o uso de mensageria para desacoplar processos da regra principal.

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
```

Estrutura principal:

```text
src/main/java/br/com/neves/approvalflowapi
├── controller
├── dto
├── entity
├── exception
├── mensageria
├── repository
└── services
```

## Funcionalidades

- Criar solicitação
- Listar solicitações
- Filtrar solicitações por status
- Buscar solicitação por ID
- Atualizar solicitação pendente
- Aprovar solicitação
- Reprovar solicitação
- Publicar evento de solicitação aprovada
- Publicar evento de solicitação reprovada
- Consumir eventos via RabbitMQ
- Padronizar respostas de erro
- Documentar endpoints com Swagger
- Testar regras de negócio com JUnit e Mockito

## Regras de negócio

Uma solicitação sempre nasce com status `PENDENTE`.

Status possíveis:

```text
PENDENTE
APROVADO
REJEITADO
```

Regras principais:

- Apenas solicitações pendentes podem ser atualizadas.
- Apenas solicitações pendentes podem ser aprovadas.
- Apenas solicitações pendentes podem ser reprovadas.
- Ao aprovar uma solicitação, um evento é publicado no RabbitMQ.
- Ao reprovar uma solicitação, um evento é publicado no RabbitMQ.

## Endpoints principais

Base URL:

```text
http://localhost:8080
```

### Criar solicitação

```http
POST /api/solicitacoes
```

Body:

```json
{
  "nomeSolicitante": "Bruno Neves",
  "descricao": "Compra de notebook para desenvolvimento",
  "valor": 4500.00
}
```

Resposta esperada:

```json
{
  "id": 1,
  "nomeSolicitante": "Bruno Neves",
  "descricao": "Compra de notebook para desenvolvimento",
  "valor": 4500.00,
  "status": "PENDENTE",
  "dataCriacao": "2026-07-05T22:00:00"
}
```

### Listar solicitações

```http
GET /api/solicitacoes
```

### Filtrar por status

```http
GET /api/solicitacoes?status=PENDENTE
```

Também é possível filtrar por:

```text
APROVADO
REJEITADO
```

### Buscar por ID

```http
GET /api/solicitacoes/{id}
```

Exemplo:

```http
GET /api/solicitacoes/1
```

### Atualizar solicitação

```http
PUT /api/solicitacoes/{id}
```

Body:

```json
{
  "descricao": "Compra de notebook para desenvolvimento Java e Angular",
  "valor": 4800.00
}
```

Observação: apenas solicitações com status `PENDENTE` podem ser atualizadas.

### Aprovar solicitação

```http
PATCH /api/solicitacoes/{id}/aprovar
```

Exemplo:

```http
PATCH /api/solicitacoes/1/aprovar
```

Ao aprovar, a API altera o status para `APROVADO` e publica um evento no RabbitMQ.

### Reprovar solicitação

```http
PATCH /api/solicitacoes/{id}/reprovar
```

Exemplo:

```http
PATCH /api/solicitacoes/1/reprovar
```

Ao reprovar, a API altera o status para `REJEITADO` e publica um evento no RabbitMQ.

## Mensageria com RabbitMQ

O projeto utiliza RabbitMQ para publicar eventos quando uma solicitação é aprovada ou reprovada.

Exchange:

```text
solicitacoes.exchange
```

Filas:

```text
solicitacao.aprovada.queue
solicitacao.reprovada.queue
```

Routing keys:

```text
solicitacao.aprovada
solicitacao.reprovada
```

Fluxo da mensageria:

```text
Solicitação aprovada/reprovada
↓
Service salva alteração
↓
Publisher envia evento para RabbitMQ
↓
Consumer recebe evento
↓
Evento é processado de forma assíncrona
```

No projeto atual, o consumer recebe o evento e registra as informações no console da aplicação.

Em um cenário real, esse evento poderia ser usado para:

- Enviar notificação por e-mail
- Registrar histórico de auditoria
- Integrar com outro sistema
- Gerar logs de negócio
- Atualizar dashboards internos

## Como rodar o projeto

### Pré-requisitos

Antes de começar, é necessário ter instalado:

- Java 21
- Maven
- Docker Desktop
- Git

### Clonar o repositório

```bash
git clone https://github.com/brunonevesyt/approval-flow-api.git
cd approval-flow-api
```

### Subir o RabbitMQ

Na raiz do projeto, execute:

```bash
docker compose up -d
```

O painel do RabbitMQ ficará disponível em:

```text
http://localhost:15672
```

Credenciais padrão:

```text
Usuário: guest
Senha: guest
```

### Rodar a aplicação

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Swagger

A documentação da API pode ser acessada em:

```text
http://localhost:8080/swagger-ui/index.html
```

Pelo Swagger é possível testar os endpoints de criação, listagem, atualização, aprovação e reprovação de solicitações.

## H2 Console

O projeto utiliza H2 em memória para facilitar o desenvolvimento local.

Acesse:

```text
http://localhost:8080/h2-console
```

Configurações:

```text
JDBC URL: jdbc:h2:mem:approvalflow
User: sa
Password:
```

## Health Check

O Actuator disponibiliza endpoint de saúde da aplicação:

```text
http://localhost:8080/actuator/health
```

Resposta esperada:

```json
{
  "status": "UP"
}
```

## Rodar testes

Para executar os testes unitários:

```bash
mvn test
```

Os testes cobrem cenários como:

- Criar solicitação
- Listar solicitações
- Buscar solicitação por ID
- Lançar exceção quando solicitação não existe
- Atualizar solicitação pendente
- Bloquear atualização de solicitação aprovada
- Aprovar solicitação pendente
- Bloquear aprovação duplicada
- Reprovar solicitação pendente
- Bloquear reprovação duplicada
- Publicar evento ao aprovar solicitação
- Publicar evento ao reprovar solicitação

## Exemplo de erro padronizado

Quando uma regra de negócio é violada, a API retorna uma resposta padronizada:

```json
{
  "dataHora": "2026-07-05T22:00:00",
  "status": 400,
  "erro": "Requisição inválida",
  "mensagem": "Somente solicitações pendentes podem ser aprovadas.",
  "caminho": "/api/solicitacoes/1/aprovar"
}
```

Exemplo de erro para recurso não encontrado:

```json
{
  "dataHora": "2026-07-05T22:00:00",
  "status": 404,
  "erro": "Não encontrado",
  "mensagem": "Solicitação não encontrada.",
  "caminho": "/api/solicitacoes/99"
}
```

## Decisões técnicas

### DTOs

Foram usados DTOs para separar os dados recebidos e retornados pela API da entidade principal da aplicação.

Isso evita expor diretamente a entidade do banco e permite controlar melhor o contrato da API.

### Service

A camada de service concentra o fluxo da aplicação, chamando repositórios, regras de negócio e publicação de eventos.

### Entity

A entidade `Solicitacao` contém regras importantes do domínio, como aprovar, reprovar e atualizar apenas quando permitido.

### Repository

O repository usa Spring Data JPA para abstrair o acesso ao banco de dados.

### Tratamento global de exceções

O projeto possui um tratador global de exceções para padronizar respostas de erro e melhorar a experiência de consumo da API.

### Mensageria

A mensageria foi adicionada para demonstrar comunicação assíncrona.

A API não precisa executar todos os processos imediatamente na mesma requisição. Ela altera o status da solicitação e publica um evento para que outro componente possa processar depois.

## Como explicar este projeto

Resumo técnico:

```text
Este projeto é uma API REST em Java com Spring Boot para gerenciar solicitações internas.
A aplicação segue arquitetura em camadas, usando Controller, Service, Repository, DTO e Entity.
As regras de negócio ficam protegidas na entidade Solicitacao.
Também implementei validações, tratamento global de exceções, documentação com Swagger, testes unitários com JUnit e Mockito e mensageria com RabbitMQ para publicar eventos quando uma solicitação é aprovada ou reprovada.
```

Resumo sobre mensageria:

```text
Usei RabbitMQ para desacoplar o fluxo principal de possíveis processos secundários.
Quando uma solicitação é aprovada ou reprovada, a API salva a alteração e publica um evento.
Um consumer escuta a fila correspondente e processa esse evento de forma assíncrona.
```

Resumo sobre testes:

```text
Os testes unitários validam as principais regras de negócio do service, usando Mockito para mockar o repository e o publisher de eventos.
Assim, consigo testar a lógica da aplicação sem depender de banco real ou RabbitMQ rodando.
```

## Próximas melhorias

- Criar frontend em Angular
- Criar autenticação com JWT
- Criar tabela de histórico/auditoria
- Persistir eventos processados
- Adicionar Dockerfile da API
- Criar pipeline CI/CD com Jenkins
- Publicar aplicação em ambiente de nuvem
- Trocar H2 por SQL Server ou Oracle em ambiente externo
- Adicionar testes de integração
- Adicionar profiles para ambiente local, teste e produção
