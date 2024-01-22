package com.test.cadastroPessoas.controllers;

import com.test.cadastroPessoas.entities.Pessoa;
import com.test.cadastroPessoas.repositories.PessoaRepository;
import com.test.cadastroPessoas.services.PessoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/pessoas")
public class CadastroPessoaController {

    final PessoaRepository pessoaRepository;

    final PessoaService pessoaService;

    public CadastroPessoaController(PessoaRepository pessoaRepository, PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaService = pessoaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> encontrarPessoaPorId(@PathVariable Long id) {
        try {
            Optional<Pessoa> pessoaOptional = pessoaService.encontrarPessoaPorId(id);

            if (pessoaOptional.isPresent()) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pessoa não encontrada com o ID: " + id);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Pessoa>> getPessoas(@RequestParam(required = false) String filtro, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamanho) {

        if (filtro == null) {
            filtro = "";
        }

        List<Pessoa> pessoas;

        try {
            pessoas = pessoaService.getPessoas(filtro, pagina, tamanho);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(pessoas);
    }

    @PostMapping("/criar")
    public ResponseEntity<Object> criarNovaPessoa(@RequestBody Pessoa pessoa) {
        try {
            Pessoa pessoaCriada = pessoaService.criarPessoa(pessoa).get();
            return new ResponseEntity<>(pessoaCriada, HttpStatus.CREATED);
        } catch (Exception e) {
            String mensagemDeErro = "Erro ao criar a pessoa: " + e.getMessage();
            return new ResponseEntity<>(mensagemDeErro, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Object> atualizarPessoa(@PathVariable Long id, @RequestBody Pessoa pessoaAtualizada) {

        Pessoa pessoa;

        try {
            pessoa = pessoaService.atualizarPessoa(id, pessoaAtualizada).get();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pessoa, HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Pessoa> deletarPessoa(@PathVariable Long id) {
        try {
            pessoaService.deletarPessoa(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }
}
