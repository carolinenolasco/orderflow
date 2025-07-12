ALTER TABLE item_pedido ALTER COLUMN id DROP DEFAULT;

ALTER TABLE item_pedido ALTER COLUMN id TYPE bigint USING id::bigint;

CREATE SEQUENCE IF NOT EXISTS item_pedido_id_seq;

ALTER TABLE item_pedido ALTER COLUMN id SET DEFAULT nextval('item_pedido_id_seq');

SELECT setval('item_pedido_id_seq', COALESCE((SELECT MAX(id) FROM item_pedido), 1), false);
