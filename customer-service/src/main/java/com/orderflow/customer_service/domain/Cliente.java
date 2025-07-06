package com.orderflow.customer_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatorio")
    private String nome;

    @NotBlank(message = "CPF é obrigatorio")
    @Column(unique = true)
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 ´digitos numéricos")
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotBlank(message = "Endereço é obrigatorio")
    private String endereco;
}
