-- Adiciona campos faltantes na tabela de projetos
-- semestre, tipo, empresa_parceira, status

-- Adiciona coluna semestre (com default temporário, depois remove o default)
alter table tb_softfact_projeto
    add column if not exists semestre varchar(20);

-- Atualiza registros existentes com valor padrão
update tb_softfact_projeto
set semestre = '2025.1'
where semestre is null;

-- Torna a coluna obrigatória
alter table tb_softfact_projeto
    alter column semestre set not null;

-- Adiciona coluna tipo
alter table tb_softfact_projeto
    add column if not exists tipo varchar(20);

-- Atualiza registros existentes
update tb_softfact_projeto
set tipo = 'AUTORAL'
where tipo is null;

-- Torna a coluna obrigatória
alter table tb_softfact_projeto
    alter column tipo set not null;

-- Adiciona coluna empresa_parceira (opcional)
alter table tb_softfact_projeto
    add column if not exists empresa_parceira varchar(200);

-- Adiciona coluna status
alter table tb_softfact_projeto
    add column if not exists status varchar(20);

-- Atualiza registros existentes
update tb_softfact_projeto
set status = 'PLANEJADO'
where status is null;

-- Torna a coluna obrigatória
alter table tb_softfact_projeto
    alter column status set not null;

-- Cria constraint de unicidade: título único por semestre
alter table tb_softfact_projeto
    drop constraint if exists uk_projeto_titulo_semestre;

alter table tb_softfact_projeto
    add constraint uk_projeto_titulo_semestre unique (titulo, semestre);

-- Cria tabela de relacionamento projeto-stack (stacks desejadas)
create table if not exists tb_softfact_projeto_stack (
    projeto_id bigint not null references tb_softfact_projeto(id) on delete cascade,
    stack_id bigint not null references tb_softfact_stack(id) on delete restrict,
    primary key (projeto_id, stack_id)
);

-- Cria índices para melhor performance
create index if not exists idx_projeto_semestre on tb_softfact_projeto (semestre);
create index if not exists idx_projeto_status on tb_softfact_projeto (status);
create index if not exists idx_projeto_tipo on tb_softfact_projeto (tipo);

