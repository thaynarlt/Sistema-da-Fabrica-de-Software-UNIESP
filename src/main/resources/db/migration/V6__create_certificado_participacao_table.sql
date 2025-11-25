-- Cria tabela de certificados de participação
-- Certificados emitidos pela Fábrica para alunos que concluíram participação em projetos

create table if not exists tb_softfact_certificado_part (
    id bigserial primary key,
    aluno_id bigint not null,
    projeto_id bigint not null,
    alocacao_id bigint not null,
    codigo_verificacao varchar(80) not null,
    versao integer not null default 1,
    ativo boolean not null default true,
    pdf_url varchar(300),
    constraint fk_cert_part_aluno
        foreign key (aluno_id) references tb_softfact_aluno(id)
        on delete cascade,
    constraint fk_cert_part_projeto
        foreign key (projeto_id) references tb_softfact_projeto(id)
        on delete cascade,
    constraint fk_cert_part_alocacao
        foreign key (alocacao_id) references tb_softfact_alocacao(id)
        on delete cascade,
    constraint uk_cert_part_codigo unique (codigo_verificacao)
);

-- Cria índices para melhor performance
create index if not exists idx_cert_part_aluno on tb_softfact_certificado_part (aluno_id);
create index if not exists idx_cert_part_projeto on tb_softfact_certificado_part (projeto_id);
create index if not exists idx_cert_part_alocacao on tb_softfact_certificado_part (alocacao_id);
create index if not exists idx_cert_part_codigo on tb_softfact_certificado_part (codigo_verificacao);
create index if not exists idx_cert_part_ativo on tb_softfact_certificado_part (ativo);

