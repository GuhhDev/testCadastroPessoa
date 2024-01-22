package com.test.cadastroPessoas.controllersTest;

import com.test.cadastroPessoas.controllers.CadastroPessoaController;
import com.test.cadastroPessoas.entities.Contato;
import com.test.cadastroPessoas.entities.Pessoa;
import com.test.cadastroPessoas.repositories.PessoaRepository;
import com.test.cadastroPessoas.services.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@DataJpaTest
class CadastroPessoaControllerTest {

    private PessoaService pessoaService;
    private CadastroPessoaController controller;

    @BeforeEach
    void setUp() {
        pessoaService = mock(PessoaService.class);
        PessoaRepository pessoaRepository = mock(PessoaRepository.class);
        controller = new CadastroPessoaController(pessoaRepository, pessoaService);
    }


    // encontrarPessoaPorId() - Testes
    @Test
    void testEncontrarPessoaPorIdExistente() {
        // Arrange
        PessoaService pessoaService = mock(PessoaService.class);
        PessoaRepository pessoaRepository = mock(PessoaRepository.class);
        CadastroPessoaController pessoaController = new CadastroPessoaController(pessoaRepository, pessoaService);

        Long id = 1L;
        when(pessoaService.encontrarPessoaPorId(id)).thenReturn(Optional.of(new Pessoa(id, "Caroline", "101.915.289-35", LocalDate.of(1999, 10, 10), Collections.emptyList())));

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.encontrarPessoaPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(pessoaService).encontrarPessoaPorId(id);
    }

    @Test
    void testEncontrarPessoaPorIdNaoExistente() {
        // Arrange
        PessoaService pessoaService = mock(PessoaService.class);
        PessoaRepository pessoaRepository = mock(PessoaRepository.class);
        CadastroPessoaController pessoaController = new CadastroPessoaController(pessoaRepository, pessoaService);

        Long id = 100L;
        when(pessoaService.encontrarPessoaPorId(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.encontrarPessoaPorId(id);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(pessoaService).encontrarPessoaPorId(id);
    }


    // listarPessoas() - Testes
    @Test
    void testGetPessoasSemFiltro() {
        // Arrange
        int pagina = 0;
        int tamanho = 10;
        List<Pessoa> pessoasMock = Arrays.asList(new Pessoa(), new Pessoa());
        when(pessoaService.getPessoas("", pagina, tamanho)).thenReturn(pessoasMock);

        // Act
        ResponseEntity<List<Pessoa>> responseEntity = controller.getPessoas(null, pagina, tamanho);

        // Assert
        assertEquals(pessoasMock, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetPessoasComFiltro() {
        // Arrange
        String filtro = "gustavo";
        int pagina = 0;
        int tamanho = 10;
        List<Pessoa> pessoasMock = Arrays.asList(new Pessoa(), new Pessoa());
        when(pessoaService.getPessoas(filtro, pagina, tamanho)).thenReturn(pessoasMock);

        // Act
        ResponseEntity<List<Pessoa>> responseEntity = controller.getPessoas(filtro, pagina, tamanho);

        // Assert
        assertEquals(pessoasMock, responseEntity.getBody(), "A lista de pessoas não corresponde ao esperado");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(pessoaService).getPessoas(filtro, pagina, tamanho);
    }




    // criarNovaPessoa() - Testes
    @Test
    void testCriarNovaPessoaComSucesso() {
        // Arrange
        PessoaService pessoaService = mock(PessoaService.class);
        PessoaRepository pessoaRepository1 = mock(PessoaRepository.class);
        CadastroPessoaController pessoaController = new CadastroPessoaController(pessoaRepository1, pessoaService);

        Long id = 1L;
        Contato contato = new Contato(id, "Caroline", "11961127970", "caroline@example.com");
        Pessoa pessoaInput = new Pessoa(id, "Caroline", "101.915.289-35", LocalDate.of(1999, 10, 10), Collections.singletonList(contato));
        Pessoa pessoaCriadaMock = new Pessoa(id, "Caroline", "101.915.289-35", LocalDate.of(1999, 10, 10), Collections.singletonList(contato));

        when(pessoaService.criarPessoa(pessoaInput)).thenReturn(java.util.Optional.of(pessoaCriadaMock));

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.criarNovaPessoa(pessoaInput);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(pessoaCriadaMock, responseEntity.getBody());
        verify(pessoaService).criarPessoa(pessoaInput);
    }

    @Test
    void testCriarNovaPessoaComErro() {
        // Arrange
        PessoaService pessoaService = mock(PessoaService.class);
        PessoaRepository pessoaRepository1 = mock(PessoaRepository.class);
        CadastroPessoaController pessoaController = new CadastroPessoaController(pessoaRepository1, pessoaService);

        Long id = 1L;
        Contato contato = new Contato(id, "Caroline", "11961127970", "caroline@example.com");
        Pessoa pessoaInput = new Pessoa(id, "Caroline", "101.915.289-35", LocalDate.of(1999, 10, 10), Collections.singletonList(contato));

        when(pessoaService.criarPessoa(pessoaInput)).thenThrow(new RuntimeException("Erro ao criar pessoa"));

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.criarNovaPessoa(pessoaInput);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Erro ao criar a pessoa: Erro ao criar pessoa", responseEntity.getBody());
        verify(pessoaService).criarPessoa(pessoaInput);
    }



    // deletarPessoa() - Testes
    @Test
    void testDeletarPessoaComSucesso() {
        // Arrange
        PessoaService pessoaService = mock(PessoaService.class);
        PessoaRepository pessoaRepository = mock(PessoaRepository.class);
        CadastroPessoaController pessoaController = new CadastroPessoaController(pessoaRepository, pessoaService);

        Long id = 1L;

        // Act
        ResponseEntity<Pessoa> responseEntity = pessoaController.deletarPessoa(id);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(pessoaService).deletarPessoa(id);
    }

    @Test
    void testDeletarPessoaComErro() {
        // Arrange
        PessoaService pessoaService = mock(PessoaService.class);
        PessoaRepository pessoaRepository = mock(PessoaRepository.class);
        CadastroPessoaController pessoaController = new CadastroPessoaController(pessoaRepository, pessoaService);

        Long id = 1L;
        doThrow(new RuntimeException("Erro ao deletar pessoa")).when(pessoaService).deletarPessoa(id);

        // Act
        ResponseEntity<Pessoa> responseEntity = pessoaController.deletarPessoa(id);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(pessoaService).deletarPessoa(id);
    }




    // atualizarPessoa() - Testes
    @Test
    void testAtualizarPessoaComSucesso() {
        // Arrange
        PessoaService pessoaService = mock(PessoaService.class);
        PessoaRepository pessoaRepository = mock(PessoaRepository.class);
        CadastroPessoaController pessoaController = new CadastroPessoaController(pessoaRepository, pessoaService);

        Long id = 1L;
        Pessoa pessoaAtualizada = new Pessoa(id, "Caroline", "101.915.289-35", LocalDate.of(1999, 10, 10), Collections.emptyList());
        Pessoa pessoaAtualizadaMock = new Pessoa(id, "Caroline", "101.915.289-35", LocalDate.of(1999, 10, 10), Collections.emptyList());

        when(pessoaService.atualizarPessoa(id, pessoaAtualizada)).thenReturn(java.util.Optional.of(pessoaAtualizadaMock));

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.atualizarPessoa(id, pessoaAtualizada);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pessoaAtualizadaMock, responseEntity.getBody());
        verify(pessoaService).atualizarPessoa(id, pessoaAtualizada);
    }

    @Test
    void testAtualizarPessoaComErro() {
        // Arrange
        PessoaService pessoaService = mock(PessoaService.class);
        PessoaRepository pessoaRepository = mock(PessoaRepository.class);
        CadastroPessoaController pessoaController = new CadastroPessoaController(pessoaRepository, pessoaService);

        Long id = 1L;
        Pessoa pessoaAtualizada = new Pessoa(id, "Caroline", "101.915.289-35", LocalDate.of(1999, 10, 10), Collections.emptyList());

        doThrow(new RuntimeException("Erro ao atualizar pessoa")).when(pessoaService).atualizarPessoa(id, pessoaAtualizada);

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.atualizarPessoa(id, pessoaAtualizada);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Erro ao atualizar pessoa", responseEntity.getBody());
        verify(pessoaService).atualizarPessoa(id, pessoaAtualizada);
    }
}