package com.test.cadastroPessoas.repositories;

import com.test.cadastroPessoas.entities.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PessoaRepository extends CrudRepository<Pessoa, Long>, PagingAndSortingRepository<Pessoa, Long> {

    @Query("SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    Page<Pessoa> buscarComFiltro(@Param("filtro") String filtro, Pageable pageable);
}
