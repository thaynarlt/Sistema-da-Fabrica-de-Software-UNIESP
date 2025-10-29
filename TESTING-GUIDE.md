# 🧪 Guia de Testes - Fábrica Software API

## 📋 Pré-requisitos

### 1. Java 17 (JDK)
- ✅ Baixar: https://adoptium.net/temurin/releases/?version=17
- Após instalar, verificar:
```powershell
java -version
# Deve mostrar: openjdk version "17.x.x"
```

### 2. PostgreSQL
- ✅ Criar database: `fabrica-software`
- ✅ Usuário: `postgres`
- ✅ Senha: `200518`
- ✅ Porta: `5432`

```sql
-- Executar no PostgreSQL
CREATE DATABASE "fabrica-software";
```

### 3. Maven (ou usar o wrapper incluído)
- O projeto já tem `mvnw.cmd` (Windows) e `mvnw` (Linux/Mac)

---

## 🚀 Como Rodar

### Opção 1: Via Maven Wrapper (Recomendado)
```powershell
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Opção 2: Via IDE
1. Abrir o projeto no IntelliJ IDEA / Eclipse / VS Code
2. Botão direito em `FabricaSoftwareApplication.java`
3. Clicar em "Run"

### ✅ Aplicação rodando
```
Started FabricaSoftwareApplication in X.XXX seconds
Aplicação disponível em: http://localhost:8080
```

---

## 🧪 Testar com Insomnia

### Importar Collection
1. Abrir Insomnia
2. Clicar em **Application** → **Preferences** → **Data**
3. Clicar em **Import Data** → **From File**
4. Selecionar o arquivo `insomnia-collection.json`
5. Pronto! Todas as requisições estão configuradas

---

## 📝 Endpoints Disponíveis

### Base URL
```
http://localhost:8080/api
```

### 🔷 Stacks

#### GET - Listar todas as stacks
```http
GET /api/stacks
```

**Resposta:**
```json
[
  {"id": 1, "nome": "Java", "categoria": "backend"},
  {"id": 2, "nome": "Spring", "categoria": "backend"},
  {"id": 3, "nome": "Python", "categoria": "backend"},
  {"id": 4, "nome": "Django", "categoria": "backend"},
  {"id": 5, "nome": "React", "categoria": "frontend"},
  {"id": 6, "nome": "Docker", "categoria": "devops"}
]
```

---

### 👤 Alunos

#### POST - Criar aluno
```http
POST /api/alunos
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao.silva@uniesp.edu.br",
  "telefone": "(11) 98765-4321",
  "curso": "CIENCIA_DA_COMPUTACAO",
  "matricula": "2024001",
  "periodo": "P3",
  "stacksIds": [1, 2, 5]
}
```

#### GET - Listar alunos
```http
GET /api/alunos
GET /api/alunos?page=0&size=10
GET /api/alunos?q=João
```

#### GET - Buscar por ID
```http
GET /api/alunos/1
```

#### PUT - Atualizar aluno
```http
PUT /api/alunos/1
Content-Type: application/json

{
  "nome": "João Silva Santos",
  "email": "joao.silva@uniesp.edu.br",
  "telefone": "(11) 98765-4321",
  "curso": "CIENCIA_DA_COMPUTACAO",
  "matricula": "2024001",
  "periodo": "P4",
  "stacksIds": [1, 2, 3, 5]
}
```

#### DELETE - Excluir aluno
```http
DELETE /api/alunos/1
```

---

## 📊 Valores Válidos

### Cursos
- `CIENCIA_DA_COMPUTACAO`
- `ENGENHARIA_DA_COMPUTACAO`
- `SISTEMAS_DE_INFORMACAO`
- `ANALISE_E_DESENVOLVIMENTO_DE_SISTEMAS`
- `ENGENHARIA_DE_SOFTWARE`

### Períodos
- `P1`, `P2`, `P3`, `P4`, `P5`, `P6`, `P7`, `P8`, `P9`, `P10`

---

## 🎯 Cenários de Teste

### 1️⃣ Fluxo Completo
1. Listar stacks disponíveis
2. Criar 3 alunos diferentes
3. Listar todos os alunos
4. Buscar um aluno específico
5. Atualizar dados de um aluno
6. Excluir um aluno

### 2️⃣ Validações
- Tentar criar aluno com email duplicado (deve falhar)
- Tentar criar aluno com matrícula duplicada (deve falhar)
- Tentar criar aluno sem nome (deve falhar)
- Tentar criar aluno com email inválido (deve falhar)

### 3️⃣ Relacionamentos
- Criar aluno sem stacks: `"stacksIds": []`
- Criar aluno com múltiplas stacks
- Atualizar stacks de um aluno

---

## 🐛 Troubleshooting

### Erro: "No compiler is provided"
- **Causa**: Java JRE instalado em vez de JDK
- **Solução**: Instalar JDK 17 e configurar JAVA_HOME

### Erro: "Connection refused to PostgreSQL"
- **Causa**: PostgreSQL não está rodando
- **Solução**: Iniciar o serviço do PostgreSQL

### Erro: "Database does not exist"
- **Causa**: Database não foi criado
- **Solução**: Executar `CREATE DATABASE "fabrica-software";`

### Erro: "Port 8080 already in use"
- **Causa**: Porta já está em uso
- **Solução**: Matar o processo ou mudar a porta em `application.yml`

---

## 📦 Estrutura do Projeto

```
src/main/java/com/etuniesp/fabrica_software/
├── aluno/
│   ├── Aluno.java (Entidade)
│   ├── AlunoController.java (REST API)
│   ├── AlunoService.java (Interface)
│   ├── AlunoServiceImpl.java (Implementação)
│   ├── AlunoRepository.java (JPA)
│   ├── dto/
│   │   ├── AlunoCreateDTO.java
│   │   ├── AlunoUpdateDTO.java
│   │   └── AlunoResponseDTO.java
│   └── enums/
│       ├── Curso.java
│       └── Periodo.java
├── stack/
│   ├── StackTecnologia.java
│   ├── StackController.java
│   ├── StackTecRepository.java
│   └── dto/
│       └── StackResumo.java
├── certificado/
│   └── Certificado.java
└── exception/
    └── ApiExceptionHandler.java
```

---

## 🔒 Segurança (TODO)
- [ ] Implementar autenticação JWT
- [ ] Adicionar autorização por roles
- [ ] Rate limiting
- [ ] CORS configurado

## 📈 Melhorias Futuras
- [ ] Swagger/OpenAPI documentation
- [ ] Testes unitários e de integração
- [ ] CI/CD pipeline
- [ ] Docker Compose
- [ ] Cache com Redis

---

## 📞 Suporte
Em caso de dúvidas, verifique:
1. Logs da aplicação no terminal
2. Arquivo `application.yml` está correto
3. PostgreSQL está acessível
4. Porta 8080 está livre

**Happy Testing! 🎉**

