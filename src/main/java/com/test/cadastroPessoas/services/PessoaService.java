package com.test.cadastroPessoas.services;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.test.cadastroPessoas.entities.Pessoa;
import com.test.cadastroPessoas.repositories.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Optional<Pessoa> encontrarPessoaPorId(Long id) {
        try {
            return pessoaRepository.findById(id).or(() -> {
                        throw new EntityNotFoundException("Pessoa não encontrada com o ID: " + id);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível encontrar a pessoa, houve um erro. " + e.getMessage(), e);
        }
    }


    private boolean validarCPF(String cpf) {
        try {
            new CPFValidator().assertValid(cpf);
            return true;
        } catch (InvalidStateException e) {
            return false;
        }
    }

    public Optional<Pessoa> criarPessoa(Pessoa pessoa) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataNascimentoFormatada = pessoa.getDataNascimento().format(formatter);

        pessoa.setDataNascimento(LocalDate.parse(dataNascimentoFormatada, formatter));

//        if (!validarCPF(pessoa.getCpf())) {
//            throw new IllegalArgumentException("CPF inválido");
//        }

        if (pessoa.getContatos().isEmpty()) {
            throw new IllegalArgumentException("A pessoa deve possuir ao menos um contato");
        }

        try {
            return Optional.of(pessoaRepository.save(pessoa));
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar a pessoa" + e.getMessage(), e);
        }
    }

    public Optional<Pessoa> atualizarPessoa(Long id, Pessoa pessoa) {
        Pessoa pessoaExistente = pessoaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com o ID: " + id));

        pessoaExistente.setNome(pessoa.getNome());
        pessoaExistente.setCpf(pessoa.getCpf());

        try {
            return Optional.of(pessoaRepository.save(pessoaExistente));
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a pessoa");
        }
    }

    public void deletarPessoa(Long id) {
        try {
            pessoaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível deletar a pessoa");
        }
    }

    public List<Pessoa> getPessoas(String filtro, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);

        try {
            Optional<List<Pessoa>> pessoas;

            if (filtro != null && !filtro.isEmpty()) {
                pessoas = Optional.of(pessoaRepository.buscarComFiltro(filtro, pageable).getContent());
            } else {
                pessoas = Optional.of(pessoaRepository.findAll(pageable).getContent());
            }

            return pessoas.orElseThrow(() -> new RuntimeException("Não foi possível buscar as pessoas " + (filtro != null ? "com" : "sem") + " filtro"));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar as pessoas", e);
        }
    }
}
