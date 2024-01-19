package com.test.cadastroPessoas.repositories;

import com.test.cadastroPessoas.entities.Contato;
import org.springframework.data.repository.CrudRepository;

public interface ContatoRepository extends CrudRepository<Contato, Long> {
}
