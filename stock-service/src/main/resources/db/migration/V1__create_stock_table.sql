CREATE TABLE estoque (
    id SERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL CHECK (quantidade >= 0)
);
