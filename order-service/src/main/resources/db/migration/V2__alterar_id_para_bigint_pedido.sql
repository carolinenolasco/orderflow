ALTER TABLE pedido ALTER COLUMN id DROP DEFAULT;

ALTER TABLE pedido ALTER COLUMN id TYPE bigint USING id::bigint;

CREATE SEQUENCE IF NOT EXISTS pedido_id_seq;

ALTER TABLE pedido ALTER COLUMN id SET DEFAULT nextval('pedido_id_seq');

SELECT setval('pedido_id_seq', COALESCE((SELECT MAX(id) FROM pedido), 1), false);
