create table restaurante_forma_pagamento (
    restaurante_id bigint not null,
    forma_pagamento_id bigint not null,

    primary key (restaurante_id, forma_pagamento_id),
    key fk_restaurante_forma_pagamento_restaurante_id (restaurante_id),
    key fk_restaurante_forma_pagamento_forma_pagamento_id (forma_pagamento_id),
    constraint fk_restaurante_forma_pagamento_restaurante_id foreign key (restaurante_id) references restaurante (id),
    constraint fk_restaurante_forma_pagamento_forma_pagamento_id foreign key (forma_pagamento_id) references forma_de_pagamento (id)
);
