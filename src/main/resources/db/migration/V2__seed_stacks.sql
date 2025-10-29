insert into tb_softfact_stack (nome, categoria) values
                                                    ('Java',   'backend'),
                                                    ('Spring', 'backend'),
                                                    ('Python', 'backend'),
                                                    ('Django', 'backend'),
                                                    ('React',  'frontend'),
                                                    ('Docker', 'devops')
    on conflict (nome) do nothing;
