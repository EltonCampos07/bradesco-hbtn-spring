package com.example.demo.controller;

import com.example.demo.model.Produto;
import com.example.demo.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    @Test
    void deveListarTodosProdutos() {
        // Arrange
        List<Produto> produtos = Arrays.asList(
                new Produto(1L, "Notebook", 3500.00),
                new Produto(2L, "Mouse", 50.00)
        );

        when(produtoService.listarProdutos()).thenReturn(produtos);

        // Act
        List<Produto> resultado = produtoController.listarProdutos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Notebook", resultado.get(0).getNome());
        assertEquals(3500.00, resultado.get(0).getPreco());
        assertEquals("Mouse", resultado.get(1).getNome());
        assertEquals(50.00, resultado.get(1).getPreco());

        verify(produtoService, times(1)).listarProdutos();
    }

    @Test
    void deveBuscarProdutoPorId() {
        // Arrange
        Long id = 1L;
        Produto produto = new Produto(id, "Notebook", 3500.00);

        when(produtoService.buscarPorId(id)).thenReturn(produto);

        // Act
        Produto resultado = produtoController.buscarProdutoPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Notebook", resultado.getNome());
        assertEquals(3500.00, resultado.getPreco());

        verify(produtoService, times(1)).buscarPorId(id);
    }

    @Test
    void deveAdicionarProduto() {
        // Arrange
        Produto novoProduto = new Produto(null, "Teclado", 150.00);
        Produto produtoSalvo = new Produto(3L, "Teclado", 150.00);

        when(produtoService.adicionarProduto(any(Produto.class))).thenReturn(produtoSalvo);

        // Act
        Produto resultado = produtoController.adicionarProduto(novoProduto);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Teclado", resultado.getNome());
        assertEquals(150.00, resultado.getPreco());

        verify(produtoService, times(1)).adicionarProduto(any(Produto.class));
    }

    @Test
    void deveAtualizarProduto() {
        // Arrange
        Long id = 1L;
        Produto produtoAtualizado = new Produto(id, "Notebook Gamer", 4500.00);

        when(produtoService.atualizarProduto(eq(id), any(Produto.class))).thenReturn(produtoAtualizado);

        // Act
        Produto resultado = produtoController.atualizarProduto(id, produtoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Notebook Gamer", resultado.getNome());
        assertEquals(4500.00, resultado.getPreco());

        verify(produtoService, times(1)).atualizarProduto(eq(id), any(Produto.class));
    }

    @Test
    void deveDeletarProduto() {
        // Arrange
        Long id = 1L;
        doNothing().when(produtoService).deletarProduto(id);

        // Act
        String resultado = produtoController.deletarProduto(id);

        // Assert
        assertEquals("Produto deletado com sucesso", resultado);

        verify(produtoService, times(1)).deletarProduto(id);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoForEncontrado() {
        // Arrange
        Long id = 999L;

        when(produtoService.buscarPorId(id))
                .thenThrow(new RuntimeException("Produto não encontrado"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            produtoController.buscarProdutoPorId(id);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(produtoService, times(1)).buscarPorId(id);
    }
}

