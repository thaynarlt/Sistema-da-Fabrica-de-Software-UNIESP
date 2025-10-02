> **Grupo:** Eduardo Bellini e Thayná Rodrigues

# **Relato do Usuário – Sistema da Fábrica de Software da UNIESP**

A Uniesp mantém uma **Fábrica de Software** vinculada às disciplinas práticas. Nela, alunos participam de projetos sob orientação de professores que atuam como tech-leaders. Quem organiza tudo é o **ordenador (admin), responsável por cadastros, alocações e emissão de certificados de participação ao final de cada semestre.** O texto abaixo descreve o que esperamos do sistema, em linguagem de usuário, para que a equipe técnica modele o banco e implemente o back-end.

### **Objetivo Geral**

O sistema deve apoiar o ciclo semestral da Fábrica: 

- cadastrar pessoas e projetos,
- relacionar alunos a projetos de acordo com suas stacks de conhecimento,
- registrar documentos pertinentes dos alunos (como certificados externos de cursos),
- acompanhar a participação durante o semestre e,
- ao final, emitir certificados de participação padronizados,
    - com possibilidade de verificação desses certificados por meio de um link/QR.

### **Atores e Visão de Uso**

**Ordenador (Admin)**

- Mantém o cadastro institucional de alunos, professores e stacks (as tecnologias reconhecidas pela Fábrica).
- Abre e gerencia projetos por semestre.
- Realiza ou confirma a alocação de alunos em projetos, distribuindo papéis (ex.: backend, frontend, QA, DevOps) e carga horária prevista.
- Ao final do semestre, emite certificados de participação (em lote ou individualmente).

**Professor (Tech-Leader)**

- Lidera um ou mais projetos em um semestre.
- Consulta lista de alunos e suas stacks para sugerir ou aprovar alocações.
- Acompanha participação, registra observações e sinaliza que um aluno concluiu sua participação.
- Pode solicitar a emissão do certificado de um aluno do seu projeto após o encerramento da participação.

**Aluno**

- Mantém seu perfil atualizado, incluindo dados de contato e quais stacks já estudou.
- Pode anexar certificados externos (documentos que comprovem formações, não é currículo).
- Visualiza projetos dos quais participa e, ao final, baixa seu certificado de participação emitido pela Fábrica.

### **Cadastros e Informações Esperadas**

**Alunos:** Registrar nome completo, e-mail institucional, telefone, curso, matrícula e período/semestre/quadri. O aluno informa as stacks que já estudou (Java, Spring, Python, Django, React, Docker etc.) e pode anexar certificados externos com breve descrição e data de envio.

**Professores (Tech-Leaders):** Registrar nome e e-mail. Cada professor pode liderar vários projetos; cada projeto tem um único tech-leader.

**Stacks:** Catálogo de tecnologias/competências (backend, frontend, dados, devops, mobile), identificadas por nome e eventualmente categoria. Associam-se a alunos (o que estudou) e a projetos (o que o projeto deseja).

**Projetos:** Vinculados a um semestre (ex.: 2025.1), com nome, descrição e tipo (empresa parceira ou autoral). Para empresa parceira, registrar o nome da empresa. O projeto define stacks desejadas e status (planejado, em andamento, concluído, cancelado). Evitar nomes repetidos no mesmo semestre.

**Alocação de alunos em projetos:** Ao incluir um aluno em um projeto, registrar data de início, papel (Dev Backend, QA etc.) и carga horária prevista/realizada. Encerrar com data de fim quando aplicável. Um aluno pode estar em mais de um projeto no mesmo semestre, respeitando limites de carga horária.

### **Funcionamento Esperado (Fluxos)**

**Início do semestre**

- Ordenador revisa/cadastra stacks, professores e projetos do semestre.
- Alunos atualizam perfil e stacks; se desejarem, anexam certificados externos.
- Professores verificam candidatos adequados pelas stacks.

**Alocação**

- Ordenador/professor inclui o aluno no projeto apropriado, registra papel, data de início e carga horária.
- Um aluno pode estar em projetos diferentes no mesmo semestre, evitando sobrecarga de horas.

**Acompanhamento**

- Professores consultam time, papéis, datas e horas.
- Saída antecipada do aluno registra data de fim e horas realizadas.

**Encerramento e certificados**

- Ao concluir projeto ou participação, marca-se a alocação como concluída.
- Emite-se certificado de participação (template padronizado, ex.: HTML/Thymeleaf convertido em PDF) com nome do aluno, projeto, tech-leader, semestre e carga horária.
- O certificado apresenta nome da Fábrica, logotipo/brasão e um código de verificação (hash) com QR code para página pública de validação.

### **Regras e Expectativas de Negócio**

- Certificado só é emitido se a participação estiver encerrada (ou projeto concluído) e houver carga horária registrada.
- Alunos e projetos devem estar associados a stacks (aluno: o que estudou; projeto: o que precisa).
- Certificados externos anexados pelos alunos são documentos de apoio; não substituem o certificado oficial da Fábrica.
- Ordenador pode emitir certificados em lote e corrigir/republicar em caso de erro, mantendo histórico e indicação de versão válida na verificação pública.
- Evitar duplicidades de cadastros (e-mails, matrículas). Cada projeto tem um professor responsável por semestre.
- Nome de projeto pode repetir em semestres diferentes, mas não no mesmo semestre.

### **Exemplos de Situações Reais**

**Exemplo 1:** A aluna Ana, do quadri 2025.1, marcou que estudou Java, Spring e Docker. O professor Carlos abriu o projeto “Plataforma de Estágios” para 2025.1, com stacks desejadas Java, Spring e Postgres. O ordenador alocou Ana como Dev Backend em 01/03, carga horária prevista de 120h. Em 30/06, Ana concluiu participação com 120h. O sistema emitiu o certificado com nome da aluna, projeto, professor (tech-leader), semestre 2025.1 e 120h, com QR code para verificação pública.

**Exemplo 2:** O aluno Bruno anexou um certificado externo de um bootcamp em Docker. Esse documento fica no perfil de Bruno, mas não é o certificado oficial da Fábrica. Quando Bruno concluir sua participação no projeto “API para Biblioteca Digital”, a Fábrica emitirá o certificado correspondente.

### **Itens Desejados nas Telas (Expectativas)**

- Busca de alunos por nome, e-mail, quadri/período e por stack estudada.
- Lista de projetos filtrável por semestre, status e stacks desejadas, indicando o professor líder.
- Visão do projeto mostrando time alocado, papéis, datas e horas.
- Área do aluno para anexar certificados externos e acompanhar participações e certificados.
- Emissão de certificados individual ou em lote, com código de verificação e QR.
- Página pública de verificação exibindo informações essenciais do certificado.

### **Considerações Finais do Usuário**

O sistema deve priorizar **a organização do semestre**, a **rastreabilidade da participação dos alunos** e a **confiabilidade dos certificados.** Cada semestre é um ciclo, e ao final deve ser possível comprovar com precisão: quem participou, em qual projeto, com qual professor e por quantas horas, com possibilidade de verificação pública do certificado.

---