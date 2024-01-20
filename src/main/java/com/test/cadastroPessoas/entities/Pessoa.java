package com.test.cadastroPessoas.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @NotBlank
    @CPF(message = "CPF inválido")
    private String cpf;

    @NotNull(message = "A data de nascimento não pode ser nula")
    @Past(message = "A data de nascimento não pode ser uma data futura")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotEmpty(message = "A pessoa deve possuir ao menos um contato")
    private List<Contato> contatos = new ArrayList<>();

}
