-- Cria tabela de alocação (participação de alunos em projetos)

create table if not exists tb_softfact_alocacao (
    id bigserial primary key,
    aluno_id bigint not null,
    projeto_id bigint not null,
    papel varchar(30) not null,
    data_inicio date not null,
    data_fim date,
    carga_horaria_prevista integer not null,
    carga_horaria_realizada integer,
    status varchar(20) not null default 'ATIVA',
    observacoes text,
    constraint fk_alocacao_aluno
        foreign key (aluno_id) references tb_softfact_aluno(id)
        on delete cascade,
    constraint fk_alocacao_projeto
        foreign key (projeto_id) references tb_softfact_projeto(id)
        on delete cascade
);

-- Cria índices para melhor performance
create index if not exists idx_alocacao_aluno on tb_softfact_alocacao (aluno_id);
create index if not exists idx_alocacao_projeto on tb_softfact_alocacao (projeto_id);
create index if not exists idx_alocacao_status on tb_softfact_alocacao (status);
create index if not exists idx_alocacao_data_inicio on tb_softfact_alocacao (data_inicio);

-- Remove a tabela ManyToMany direta projeto-aluno (se existir)
-- A relação agora será através de Alocacao
-- Mas vamos manter a tabela por enquanto para não quebrar dados existentes
-- drop table if exists tb_softfact_projeto_aluno;

