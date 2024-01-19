package com.test.cadastroPessoas.services;

import com.test.cadastroPessoas.entities.Pessoa;
import com.test.cadastroPessoas.repositories.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Optional<Pessoa> encontrarPessoaPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    public Optional<Pessoa> criarPessoa(Pessoa pessoa) {
        return Optional.of(pessoaRepository.save(pessoa));
    }

    public Optional<Pessoa> atualizarPessoa(Long id, Pessoa pessoa) {
        Pessoa pessoaExistente = pessoaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com o ID: " + id));

        pessoaExistente.setNome(pessoa.getNome());
        pessoaExistente.setCpf(pessoa.getCpf());

        return Optional.of(pessoaRepository.save(pessoaExistente));
    }

    public void deletarPessoa(Long id) {
        pessoaRepository.deleteById(id);
    }

    public List<Pessoa> getPessoas(String filtro, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        if (filtro != null && !filtro.isEmpty()) {
            return pessoaRepository.buscarComFiltro(filtro, pageable).getContent();
        } else {
            return pessoaRepository.findAll(pageable).getContent();
        }
    }
}
