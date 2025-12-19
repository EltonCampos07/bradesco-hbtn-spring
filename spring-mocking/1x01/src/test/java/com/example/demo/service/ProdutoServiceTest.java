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
        Long id = 1L;
        Produto produtoEsperado = new Produto(id, "Notebook", 3500.00);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoEsperado));

        Produto produtoRetornado = produtoService.buscarPorId(id);

        assertNotNull(produtoRetornado);
        assertEquals(id, produtoRetornado.getId());
        assertEquals("Notebook", produtoRetornado.getNome());
        assertEquals(3500.00, produtoRetornado.getPreco());

        verify(produtoRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        Long id = 999L;

        when(produtoRepository.findById(id)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            produtoService.buscarPorId(id);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(produtoRepository, times(1)).findById(id);
    }
}