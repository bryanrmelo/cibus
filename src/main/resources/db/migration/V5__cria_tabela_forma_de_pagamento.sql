create table forma_de_pagamento (
    id bigint auto_increment,
    nome varchar(50) not null,
    primary key (id)
);

alter table forma_de_pagamento add constraint uk_forma_de_pagamento_nome unique (nome);
