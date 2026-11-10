# Desafio — Validação e Segurança

## 📄 Descrição

Este projeto implementa uma API REST com Spring Boot para cidades e eventos, com validação dos dados de entrada e controle de acesso por token JWT, desenvolvida para fazer passar os testes de integração do projeto base do desafio.

## 🎯 Objetivo do desafio

Implementar as regras de validação e de controle de acesso necessárias para que os testes do projeto base passem.

## 🛠 Tecnologias utilizadas

- Java 25
- Spring Boot 4.1.1
- Spring Data JPA
- Hibernate
- Bean Validation
- Spring Security (Resource Server com JWT)
- Banco de dados H2
- JUnit

## 📐 Modelo de Domínio

- **User**: `userId`, `email` (único), `password`, com um ou mais papéis
- **Role**: `roleId`, `authority`
- **City**: `cityId`, `name`
- **Event**: `eventId`, `name`, `date`, `url`
- Relacionamento: Event → City (ManyToOne)

No JSON, os ids continuam como `id`, como no contrato do curso.

## 📦 Carga Inicial do Banco de Dados

A aplicação carrega automaticamente 11 cidades, 4 eventos e 2 usuários:

| Usuário | Senha | Papéis |
|---|---|---|
| `ana@gmail.com` | `123456` | CLIENT |
| `bob@gmail.com` | `123456` | CLIENT, ADMIN |

## 🔎 Funcionalidades

### Login

| Método | Endpoint | Acesso |
|---|---|---|
| POST | /oauth2/token | Cliente da aplicação (Basic) |

### Cidades

| Método | Endpoint | Acesso |
|---|---|---|
| GET | /cities | Público |
| POST | /cities | ADMIN |

### Eventos

| Método | Endpoint | Acesso |
|---|---|---|
| GET | /events | Público |
| POST | /events | ADMIN, CLIENT |

- A listagem de cidades é ordenada pelo nome.
- A listagem de eventos é paginada (`content` e `page`).

## ⚠️ Tratamento de Exceções

Os erros seguem o formato `ProblemDetail` (RFC 9457).

- `400 Bad Request` — cidade inexistente na inserção do evento, `grant_type` diferente de `password` ou parâmetro obrigatório ausente
- `401 Unauthorized` — token ausente ou inválido, ou credenciais de login inválidas
- `403 Forbidden` — usuário logado sem o papel exigido pela rota
- `422 Unprocessable Content` — erro de validação, com uma entrada por erro na propriedade `errors` (`fieldName` e `message`)
  - cidade — `name`: obrigatório
  - evento — `name`: obrigatório; `date`: não pode ser passada; `cityId`: obrigatório

## 🔐 Segurança

- **Login:** o `POST /oauth2/token` valida o cliente da aplicação (Basic) e o usuário (`username` e `password` no formulário), e devolve um JWT com as claims `username` e `authorities`.

## ⚙️ Variáveis de ambiente

| Variável | Padrão | Uso |
|---|---|---|
| `CLIENT_ID` | `myclientid` | Id do cliente da aplicação no login |
| `CLIENT_SECRET` | `myclientsecret` | Segredo do cliente da aplicação no login |
| `JWT_DURATION` | `86400` | Duração do token, em segundos |
| `CORS_ORIGINS` | `http://localhost:3000,http://localhost:5173` | Origens liberadas para o front-end |

Os valores padrão de `CLIENT_ID` e `CLIENT_SECRET` são os do ambiente do Postman do curso.

## 🚀 Como Executar

1. Clonar o repositório
2. Importar o projeto em sua IDE como projeto Maven
3. Executar a classe principal da aplicação (anotada com `@SpringBootApplication`) pela IDE ou via terminal:

```bash
./mvnw spring-boot:run
```

## ✅ Validação

Após executar a aplicação, o banco de dados pode ser acessado via H2 Console para verificação das tabelas e dados inseridos.

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuário: sa
- Senha: (vazia)

## 🧪 Testes

Os testes de integração são os do projeto base do desafio e cobrem o controle de acesso (401 e 403), as validações (422), as inserções e as listagens de cidades e eventos:

```bash
./mvnw clean verify
```

## 📌 Exemplos de Requisições

### Login

```http
POST /oauth2/token
Authorization: Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==
Content-Type: application/x-www-form-urlencoded
```

```
grant_type=password&username=bob@gmail.com&password=123456
```

Nas requisições seguintes, o token vai no cabeçalho `Authorization: Bearer <access_token>`.

### Listagem de cidades

```http
GET /cities
```

### Inserção de cidade (ADMIN)

```http
POST /cities
```

```json
{
  "name": "Recife"
}
```

### Listagem paginada de eventos

```http
GET /events?page=0&size=12&sort=name
```

### Inserção de evento (ADMIN, CLIENT)

```http
POST /events
```

```json
{
  "name": "Expo XP",
  "date": "2030-07-15",
  "url": "https://expoxp.com.br",
  "cityId": 1
}
```
