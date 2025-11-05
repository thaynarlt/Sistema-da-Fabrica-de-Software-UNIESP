create table if not exists tb_softfact_professor (
    id bigserial primary key,
    nome varchar(255) not null,
    email varchar(255) not null,
    telefone varchar(100),
    area varchar(255),
    constraint uk_professor_email unique (email)
);


create table if not exists tb_softfact_professor_stack (
    professor_id bigint not null references tb_softfact_professor(id) on delete cascade,
    stack_id bigint not null references tb_softfact_stack(id) on delete restrict,
    primary key (professor_id, stack_id)
);


create table if not exists tb_softfact_projeto (
    id bigserial primary key,
    titulo varchar(255) not null,
    descricao text,
    data_inicio date,
    data_fim date,
    professor_id bigint references tb_softfact_professor(id) on delete set null
);

create table if not exists tb_softfact_projeto_aluno (
    projeto_id bigint not null references tb_softfact_projeto(id) on delete cascade,
    aluno_id bigint not null references tb_softfact_aluno(id) on delete restrict,
    primary key (projeto_id, aluno_id)
);


