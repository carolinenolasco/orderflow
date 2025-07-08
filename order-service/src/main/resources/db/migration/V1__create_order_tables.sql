CREATE TABLE pedido (
    id SERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    data_pedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE item_pedido (
    id SERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedido(id) ON DELETE CASCADE,
    produto_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    preco_unitario NUMERIC(10, 2) NOT NULL CHECK (preco_unitario >= 0)
);
