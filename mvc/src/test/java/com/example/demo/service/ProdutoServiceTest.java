package com.example.demo.service;

import com.example.demo.model.Produto;
import com.example.demo.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveRetornarProdutoQuandoIdExistir() {
        // Arrange (Preparar)
        Long id = 1L;
        Produto produtoEsperado = new Produto(id, "Notebook", 3500.00);

        // Simula o comportamento do repositório para retornar o produto quando findById for chamado
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoEsperado));

        // Act (Agir)
        Produto produtoRetornado = produtoService.buscarPorId(id);

        // Assert (Verificar)
        assertNotNull(produtoRetornado);
        assertEquals(id, produtoRetornado.getId());
        assertEquals("Notebook", produtoRetornado.getNome());
        assertEquals(3500.00, produtoRetornado.getPreco());

        // Verifica se o método findById foi chamado exatamente uma vez com o id correto
        verify(produtoRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        // Arrange (Preparar)
        Long id = 999L;

        // Simula o comportamento do repositório para retornar Optional vazio quando o produto não existe
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert (Agir e Verificar)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            produtoService.buscarPorId(id);
        });

        // Verifica se a mensagem da exceção está correta
        assertEquals("Produto não encontrado", exception.getMessage());

        // Verifica se o método findById foi chamado exatamente uma vez com o id correto
        verify(produtoRepository, times(1)).findById(id);
    }
}

