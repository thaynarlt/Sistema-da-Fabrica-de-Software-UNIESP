# 🧪 GUIA DE TESTES - INSOMNIA

## ⚡ INÍCIO RÁPIDO

### 1️⃣ Importar Collection
1. Abrir **Insomnia**
2. Menu: **Application** → **Preferences** → **Data**
3. Clicar em **Import Data** → **From File**
4. Selecionar: `insomnia-collection.json`
5. ✅ Collection "Fabrica Software API" importada!

### 2️⃣ Verificar Base URL
- Base URL já configurada: `http://localhost:8080/api`
- Se a aplicação estiver em outra porta, edite em **Environments**

---

## 🧪 SEQUÊNCIA DE TESTES

### ✅ TESTE 1: Listar Stacks (Verificar Conexão)

**Request:** `GET /api/stacks`

**Resposta Esperada (200 OK):**
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

✅ **Se funcionou:** Banco de dados está conectado e migrations rodaram!

❌ **Se deu erro:**
- Aplicação está rodando? (`.\mvnw.cmd spring-boot:run`)
- Database existe? (`CREATE DATABASE "fabrica-software"`)
- PostgreSQL está rodando?

---

### ✅ TESTE 2: Criar Primeiro Aluno

**Request:** `POST /api/alunos`

**Body (JSON):**
```json
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

**Resposta Esperada (200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@uniesp.edu.br",
  "telefone": "(11) 98765-4321",
  "curso": "CIENCIA_DA_COMPUTACAO",
  "matricula": "2024001",
  "periodo": "P3",
  "stacks": [
    {"id": 1, "nome": "Java", "categoria": "backend"},
    {"id": 2, "nome": "Spring", "categoria": "backend"},
    {"id": 5, "nome": "React", "categoria": "frontend"}
  ]
}
```

📝 **Anote o ID retornado** (provavelmente será 1)

---

### ✅ TESTE 3: Criar Segundo Aluno

**Request:** `POST /api/alunos`

**Body (JSON):**
```json
{
  "nome": "Maria Oliveira",
  "email": "maria.oliveira@uniesp.edu.br",
  "telefone": "(11) 91234-5678",
  "curso": "ENGENHARIA_DE_SOFTWARE",
  "matricula": "2024002",
  "periodo": "P5",
  "stacksIds": [3, 4, 6]
}
```

---

### ✅ TESTE 4: Criar Terceiro Aluno

**Request:** `POST /api/alunos`

**Body (JSON):**
```json
{
  "nome": "Pedro Santos",
  "email": "pedro.santos@uniesp.edu.br",
  "telefone": "(11) 99999-8888",
  "curso": "SISTEMAS_DE_INFORMACAO",
  "matricula": "2024003",
  "periodo": "P2",
  "stacksIds": [1, 5]
}
```

---

### ✅ TESTE 5: Listar Todos os Alunos

**Request:** `GET /api/alunos`

**Resposta Esperada:** Array com os 3 alunos criados

---

### ✅ TESTE 6: Buscar Aluno por ID

**Request:** `GET /api/alunos/1`

**Resposta Esperada:** Dados do João Silva

---

### ✅ TESTE 7: Buscar com Filtro

**Request:** `GET /api/alunos?q=João`

**Resposta Esperada:** Apenas alunos com "João" no nome/email/matrícula

---

### ✅ TESTE 8: Listar com Paginação

**Request:** `GET /api/alunos?page=0&size=10`

**Resposta Esperada:** Objeto de paginação com os alunos

---

### ✅ TESTE 9: Atualizar Aluno

**Request:** `PUT /api/alunos/1`

**Body (JSON):**
```json
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

**Verificar:** Nome mudou para "João Silva Santos" e período para "P4"

---

### ✅ TESTE 10: Excluir Aluno

**Request:** `DELETE /api/alunos/3`

**Resposta Esperada:** 200 OK (sem body)

**Verificar:** Listar alunos novamente, deve ter apenas 2

---

## 🧪 TESTES DE VALIDAÇÃO

### ❌ TESTE 11: Email Duplicado (Deve Falhar)

**Request:** `POST /api/alunos`

**Body:**
```json
{
  "nome": "Outro Aluno",
  "email": "joao.silva@uniesp.edu.br",
  "telefone": "(11) 99999-9999",
  "curso": "SISTEMAS_DE_INFORMACAO",
  "matricula": "2024999",
  "periodo": "P1",
  "stacksIds": [1]
}
```

**Resposta Esperada:** Erro 400 ou 500
```json
{
  "message": "E-mail já cadastrado."
}
```

---

### ❌ TESTE 12: Matrícula Duplicada (Deve Falhar)

**Request:** `POST /api/alunos`

**Body:**
```json
{
  "nome": "Outro Aluno",
  "email": "outro@uniesp.edu.br",
  "telefone": "(11) 99999-9999",
  "curso": "SISTEMAS_DE_INFORMACAO",
  "matricula": "2024001",
  "periodo": "P1",
  "stacksIds": [1]
}
```

**Resposta Esperada:** Erro 400 ou 500
```json
{
  "message": "Matrícula já cadastrada."
}
```

---

### ❌ TESTE 13: Email Inválido (Deve Falhar)

**Request:** `POST /api/alunos`

**Body:**
```json
{
  "nome": "Teste Email",
  "email": "email-invalido",
  "telefone": "(11) 99999-9999",
  "curso": "SISTEMAS_DE_INFORMACAO",
  "matricula": "2024099",
  "periodo": "P1",
  "stacksIds": [1]
}
```

**Resposta Esperada:** Erro 400 - Validação do Bean Validation

---

### ❌ TESTE 14: Campos Obrigatórios Vazios (Deve Falhar)

**Request:** `POST /api/alunos`

**Body:**
```json
{
  "nome": "",
  "email": "teste@teste.com",
  "telefone": "",
  "curso": "SISTEMAS_DE_INFORMACAO",
  "matricula": "",
  "periodo": "P1",
  "stacksIds": []
}
```

**Resposta Esperada:** Erro 400 - Campos obrigatórios

---

### ❌ TESTE 15: Stack Inexistente (Deve Falhar)

**Request:** `POST /api/alunos`

**Body:**
```json
{
  "nome": "Teste Stack",
  "email": "teste.stack@uniesp.edu.br",
  "telefone": "(11) 99999-9999",
  "curso": "SISTEMAS_DE_INFORMACAO",
  "matricula": "2024098",
  "periodo": "P1",
  "stacksIds": [999]
}
```

**Resposta Esperada:** Erro 404 - Stack não encontrada

---

### ❌ TESTE 16: Aluno Inexistente (Deve Falhar)

**Request:** `GET /api/alunos/9999`

**Resposta Esperada:** Erro 404 - Aluno não encontrado

---

## 📊 CHECKLIST FINAL

Marque conforme for testando:

```
□ GET /api/stacks - Listar stacks
□ POST /api/alunos - Criar aluno 1 (João)
□ POST /api/alunos - Criar aluno 2 (Maria)
□ POST /api/alunos - Criar aluno 3 (Pedro)
□ GET /api/alunos - Listar todos
□ GET /api/alunos/1 - Buscar por ID
□ GET /api/alunos?q=João - Buscar com filtro
□ GET /api/alunos?page=0&size=10 - Paginação
□ PUT /api/alunos/1 - Atualizar aluno
□ DELETE /api/alunos/3 - Excluir aluno
□ POST (email duplicado) - Validação OK
□ POST (matrícula duplicada) - Validação OK
□ POST (email inválido) - Validação OK
□ POST (campos vazios) - Validação OK
□ POST (stack inexistente) - Validação OK
□ GET (aluno inexistente) - Validação OK
```

---

## 🎯 VALORES PARA COPIAR E COLAR

### Cursos:
```
CIENCIA_DA_COMPUTACAO
ENGENHARIA_DA_COMPUTACAO
SISTEMAS_DE_INFORMACAO
ANALISE_E_DESENVOLVIMENTO_DE_SISTEMAS
ENGENHARIA_DE_SOFTWARE
```

### Períodos:
```
P1, P2, P3, P4, P5, P6, P7, P8, P9, P10
```

### IDs das Stacks (do seed):
```
1 - Java (backend)
2 - Spring (backend)
3 - Python (backend)
4 - Django (backend)
5 - React (frontend)
6 - Docker (devops)
```

---

## 💡 DICAS

### Ver Logs da Aplicação
- Olhe o terminal onde rodou `.\mvnw.cmd spring-boot:run`
- Os logs SQL aparecem lá!

### Ver no Banco de Dados
```sql
-- Ver todas as stacks
SELECT * FROM tb_softfact_stack;

-- Ver todos os alunos
SELECT * FROM tb_softfact_aluno;

-- Ver relacionamento aluno-stack
SELECT * FROM tb_softfact_aluno_stack;

-- Ver alunos com suas stacks
SELECT 
    a.nome as aluno,
    a.email,
    a.curso,
    a.periodo,
    s.nome as stack,
    s.categoria
FROM tb_softfact_aluno a
JOIN tb_softfact_aluno_stack as_rel ON a.id = as_rel.aluno_id
JOIN tb_softfact_stack s ON s.id = as_rel.stack_id
ORDER BY a.nome, s.nome;
```

### Resetar Dados
```sql
-- Cuidado! Isso apaga tudo
TRUNCATE TABLE tb_softfact_aluno_stack CASCADE;
TRUNCATE TABLE tb_softfact_aluno RESTART IDENTITY CASCADE;
TRUNCATE TABLE tb_softfact_stack RESTART IDENTITY CASCADE;

-- Rodar migrations novamente
-- Reinicie a aplicação
```

---

## 🎉 PRONTO!

Agora você tem um guia completo para testar toda a API no Insomnia!

**Happy Testing! 🚀**

