package com.test.cadastroPessoas.controllers;

import com.test.cadastroPessoas.entities.Pessoa;
import com.test.cadastroPessoas.repositories.PessoaRepository;
import com.test.cadastroPessoas.services.PessoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pessoas")
public class CadastroPessoaController {

    final PessoaRepository pessoaRepository;

    final PessoaService pessoaService;

    public CadastroPessoaController(PessoaRepository pessoaRepository, PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaService = pessoaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> encontrarPessoaPorId(@PathVariable Long id) {

        Optional<Pessoa> pessoaOptional = pessoaService.encontrarPessoaPorId(id);
        return pessoaOptional.map(pessoa -> new ResponseEntity<>(pessoa, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Pessoa>> getPessoas(@RequestParam(required = false) String filtro,
                                                   @RequestParam(defaultValue = "0") int pagina,
                                                   @RequestParam(defaultValue = "10") int tamanho) {

        if (filtro == null) { filtro = ""; }

        List<Pessoa> pessoas = pessoaService.getPessoas(filtro, pagina, tamanho);
        return ResponseEntity.ok(pessoas);
    }

    @PostMapping("/criar")

    public ResponseEntity<Pessoa> criarNovaPessoa(@RequestBody Pessoa pessoa) {
    Pessoa pessoaCriada = pessoaService.criarPessoa(pessoa).orElseThrow(() -> new RuntimeException("Não foi possível criar a pessoa"));
        return new ResponseEntity<>(pessoaCriada, HttpStatus.CREATED);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable Long id, @RequestBody Pessoa pessoaAtualizada) {
        Pessoa pessoaAtual = pessoaService.atualizarPessoa(id, pessoaAtualizada).orElseThrow(() -> new RuntimeException("Não foi possível atualizar a pessoa"));
        return new ResponseEntity<>(pessoaAtual, HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Pessoa> deletarPessoa(@PathVariable Long id) {
        pessoaService.deletarPessoa(id);
        return ResponseEntity.ok().build();
    }
}
