package com.orderflow.order_service.domain;

public enum StatusPedido {
    CRIADO,
    ENVIADO,
    CANCELADO,
    FECHADO_COM_SUCESSO,
    FECHADO_SEM_CREDITO,
    FECHADO_SEM_ESTOQUE
}
