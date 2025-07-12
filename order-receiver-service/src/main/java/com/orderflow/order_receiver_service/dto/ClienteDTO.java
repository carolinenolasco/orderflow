package com.orderflow.order_receiver_service.dto;

public class ClienteDTO {
    private Long id;
    private String nome;
    // outros campos que seu serviço retornar

    // Construtor padrão
    public ClienteDTO() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    // outros getters e setters
}
