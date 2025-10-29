-- Tabelas: stacks, alunos, certificados externos, e a join aluno_stack.

create table if not exists tb_softfact_stack (
                                                 id           bigserial primary key,
                                                 nome         varchar(120) not null,
    categoria    varchar(60),
    constraint uk_stack_nome unique (nome)
    );

create table if not exists tb_softfact_aluno (
                                                 id          bigserial primary key,
                                                 nome        varchar(200) not null,
    email       varchar(200) not null,
    telefone    varchar(40),
    curso       varchar(60)  not null,
    matricula   varchar(60)  not null,
    periodo     varchar(20)  not null,
    constraint uk_aluno_email     unique (email),
    constraint uk_aluno_matricula unique (matricula)
    );

create index if not exists idx_aluno_nome      on tb_softfact_aluno (nome);
create index if not exists idx_aluno_email     on tb_softfact_aluno (email);
create index if not exists idx_aluno_matricula on tb_softfact_aluno (matricula);

create table if not exists tb_softfact_aluno_stack (
                                                       aluno_id  bigint not null,
                                                       stack_id  bigint not null,
                                                       primary key (aluno_id, stack_id),
    constraint fk_aluno_stack__aluno
    foreign key (aluno_id) references tb_softfact_aluno(id)
    on delete cascade,
    constraint fk_aluno_stack__stack
    foreign key (stack_id) references tb_softfact_stack(id)
    on delete cascade
    );

create table if not exists tb_softfact_certificado_ext (
                                                           id          bigserial primary key,
                                                           aluno_id    bigint     not null,
                                                           descricao   varchar(255) not null,
    arquivo_url varchar(500),
    data_envio  date       not null,
    constraint fk_cert_ext__aluno
    foreign key (aluno_id) references tb_softfact_aluno(id)
    on delete cascade
    );

create index if not exists idx_cert_ext_aluno on tb_softfact_certificado_ext (aluno_id);
