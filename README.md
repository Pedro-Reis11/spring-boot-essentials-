# 🏋️ API Academia - Sistema de Gerenciamento

## 📌 Sobre o Projeto

A **API Academia** é uma aplicação full stack desenvolvida para gerenciar alunos, treinos e avaliações físicas de uma academia.

O sistema permite o controle completo das entidades, com integração entre backend (Spring Boot) e um frontend simples em HTML, CSS e JavaScript para visualização e interação com a API.

---

## 🚀 Tecnologias Utilizadas

### 🔙 Backend

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Bean Validation
* Lombok
* Swagger/OpenAPI

### 🔜 Frontend

* HTML5
* CSS3
* JavaScript (Vanilla)

### 🗄️ Banco de Dados

* MySQL 

---

## 🧠 Arquitetura

O projeto segue uma arquitetura em camadas:

```
controller → service → repository → entity
                    ↓
                   dto
                    ↓
                 mapper
```

---

## 📂 Estrutura do Projeto

```
academia-system/
│
├── backend/
│   └── src/main/java/com/spring-boot-essentials/
│
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
│
└── README.md
```

---

## ⚙️ Funcionalidades

### 👥 Alunos

* Criar aluno
* Listar alunos
* Atualizar aluno
* Deletar aluno

### 📊 Avaliação Física

* Criar avaliação física
* Atualizar avaliação
* Deletar avaliação
* Listar avaliações

### 🏋️ Treinos

* Criar treino
* Listar treinos
* Listar treinos por aluno
* Associação de exercícios ao treino

### 🔗 Integração

* Frontend consumindo API via `fetch`
* Renderização dinâmica dos dados
* Controle de estado no front-end

---

## 🔌 Endpoints principais

### 📌 Alunos

```
GET    /alunos
POST   /alunos
PUT    /alunos/{id}
DELETE /alunos/{id}
```

### 📌 Avaliações

```
GET    /avaliacoes
POST   /avaliacoes
PUT    /avaliacoes/{idAluno}
DELETE /avaliacoes/{idAluno}
```

### 📌 Treinos

```
GET    /treinos
POST   /treinos
GET    /treinos/{idAluno}/treinos
```

---

## ▶️ Como Executar o Projeto

### 🔙 Backend

1. Clone o repositório:

```bash
git clone https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git
```

2. Acesse a pasta:

```bash
cd backend
```

3. Execute o projeto:

```bash
./mvnw spring-boot:run
```

ou via IDE (IntelliJ / Eclipse)

---

### 🔜 Frontend

1. Acesse a pasta:

```bash
cd frontend
```

2. Abra o arquivo:

```bash
index.html
```

---

## 📖 Documentação da API

A documentação está disponível via Swagger:

```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Exemplos de Requisição

### Criar Aluno

```json
{
  "nome": "Pedro",
  "email": "pedro@gmail.com"
}
```

### Criar Treino

```json
{
  "idAluno": 1,
  "nome": "Upper A",
  "exerciciosIds": [1, 2, 3]
}
```

### Criar Avaliação Física

```json
{
  "idAluno": 1,
  "peso": 80,
  "altura": 1.75,
  "porcentagemGorduraCorporal": 15
}
```

---

## 🧠 Aprendizados

Durante o desenvolvimento deste projeto foram aplicados conceitos importantes como:

* CRUD completo com Spring Boot
* Mapeamento de entidades com JPA
* Relacionamentos (OneToOne, OneToMany)
* DTOs e MapStruct
* Validações com Bean Validation
* Integração Frontend ↔ Backend
* Manipulação do DOM com JavaScript
* Consumo de API com `fetch`

---

## 🔮 Próximos Passos

* 🔐 Implementar autenticação com JWT
* 🧪 Adicionar testes unitários (JUnit / Mockito)
* ☁️ Deploy na AWS
* 🐳 Dockerização da aplicação
* 🎨 Melhorar UI/UX do frontend
* ⚡ Migrar frontend para React

---

## 👨‍💻 Autor

Desenvolvido por **Pedro Reis**

---
