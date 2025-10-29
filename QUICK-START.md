# ⚡ Quick Start - Fábrica Software API

## 🎯 1 Minuto para Começar

### ✅ Checklist Rápido
```
□ Java 17 instalado (não Java 8!)
□ PostgreSQL rodando
□ Database "fabrica-software" criado
□ Insomnia instalado
```

---

## 🚀 Rodar em 3 Passos

### 1️⃣ Criar Database
```sql
CREATE DATABASE "fabrica-software";
```

### 2️⃣ Rodar Aplicação
```powershell
.\mvnw.cmd spring-boot:run
```

### 3️⃣ Testar no Insomnia
- Importar arquivo: `insomnia-collection.json`
- Testar endpoint: `GET http://localhost:8080/api/stacks`

---

## 📦 O que foi corrigido?

✅ Todos os erros de import corrigidos  
✅ Pacotes ajustados de `br.edu.uniesp.softfact` → `com.etuniesp.fabrica_software`  
✅ Warnings do Lombok resolvidos  
✅ Controller de Stacks criado  
✅ Configuração do banco ajustada  

---

## 🧪 Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/stacks` | Lista todas as stacks |
| POST | `/api/alunos` | Cria um aluno |
| GET | `/api/alunos` | Lista todos os alunos |
| GET | `/api/alunos/1` | Busca aluno por ID |
| PUT | `/api/alunos/1` | Atualiza um aluno |
| DELETE | `/api/alunos/1` | Exclui um aluno |
| GET | `/api/alunos?q=João` | Busca com filtro |
| GET | `/api/alunos?page=0&size=10` | Lista com paginação |

---

## 📝 Exemplo Completo

### Criar Aluno
```json
POST http://localhost:8080/api/alunos

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

### Resposta
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

---

## 🎨 Cursos e Períodos Válidos

### Cursos
```
CIENCIA_DA_COMPUTACAO
ENGENHARIA_DA_COMPUTACAO
SISTEMAS_DE_INFORMACAO
ANALISE_E_DESENVOLVIMENTO_DE_SISTEMAS
ENGENHARIA_DE_SOFTWARE
```

### Períodos
```
P1, P2, P3, P4, P5, P6, P7, P8, P9, P10
```

---

## 🐛 Problemas Comuns

### "No compiler is provided"
➡️ **Instale Java 17 JDK**: https://adoptium.net/temurin/releases/?version=17

### "Connection refused"
➡️ **Inicie o PostgreSQL** e verifique se está na porta 5432

### "Database does not exist"
➡️ **Execute**: `CREATE DATABASE "fabrica-software";`

---

## 📚 Arquivos Criados para Você

| Arquivo | Descrição |
|---------|-----------|
| `insomnia-collection.json` | Collection completa para Insomnia |
| `TESTING-GUIDE.md` | Guia detalhado de testes |
| `setup.ps1` | Script automatizado de setup |
| `curl-examples.sh` | Exemplos com CURL (Linux/Mac) |
| `curl-examples.ps1` | Exemplos com PowerShell (Windows) |
| `QUICK-START.md` | Este arquivo! |

---

## 🎉 Pronto!

Agora é só:
1. Rodar a aplicação
2. Abrir o Insomnia
3. Importar a collection
4. Testar todos os endpoints

**Boa sorte! 🚀**

---

## 📞 Estrutura das Entidades

```
Aluno
├── id: Long
├── nome: String
├── email: String (único)
├── telefone: String
├── curso: Enum
├── matricula: String (único)
├── periodo: Enum
├── stacks: Set<StackTecnologia>
└── certificados: Set<Certificado>

StackTecnologia
├── id: Long
├── nome: String (único)
└── categoria: String

Certificado
├── id: Long
├── aluno: Aluno
├── descricao: String
├── arquivoUrl: String
└── dataEnvio: Date
```

---

**Desenvolvido com ❤️ para Fábrica de Software**

