package com.example.demo.controller;

import com.example.demo.model.Produto;
import com.example.demo.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

        when(produtoService.listarTodos()).thenReturn(produtos);

        // Act
        ResponseEntity<List<Produto>> response = produtoController.listarProdutos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Notebook", response.getBody().get(0).getNome());
        assertEquals("Mouse", response.getBody().get(1).getNome());

        verify(produtoService, times(1)).listarTodos();
    }

    @Test
    void deveBuscarProdutoPorId() {
        // Arrange
        Long id = 1L;
        Produto produto = new Produto(id, "Notebook", 3500.00);

        when(produtoService.buscarPorId(id)).thenReturn(produto);

        // Act
        ResponseEntity<Produto> response = produtoController.buscarProdutoPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("Notebook", response.getBody().getNome());
        assertEquals(3500.00, response.getBody().getPreco());

        verify(produtoService, times(1)).buscarPorId(id);
    }

    @Test
    void deveAdicionarProduto() {
        // Arrange
        Produto novoProduto = new Produto(null, "Teclado", 150.00);
        Produto produtoSalvo = new Produto(3L, "Teclado", 150.00);

        when(produtoService.salvar(any(Produto.class))).thenReturn(produtoSalvo);

        // Act
        ResponseEntity<Produto> response = produtoController.adicionarProduto(novoProduto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3L, response.getBody().getId());
        assertEquals("Teclado", response.getBody().getNome());
        assertEquals(150.00, response.getBody().getPreco());

        verify(produtoService, times(1)).salvar(any(Produto.class));
    }

    @Test
    void deveAtualizarProduto() {
        // Arrange
        Long id = 1L;
        Produto produtoAtualizado = new Produto(id, "Notebook Gamer", 4500.00);

        when(produtoService.atualizar(eq(id), any(Produto.class))).thenReturn(produtoAtualizado);

        // Act
        ResponseEntity<Produto> response = produtoController.atualizarProduto(id, produtoAtualizado);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("Notebook Gamer", response.getBody().getNome());
        assertEquals(4500.00, response.getBody().getPreco());

        verify(produtoService, times(1)).atualizar(eq(id), any(Produto.class));
    }

    @Test
    void deveDeletarProduto() {
        // Arrange
        Long id = 1L;
        doNothing().when(produtoService).deletar(id);

        // Act
        ResponseEntity<String> response = produtoController.deletarProduto(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Produto deletado com sucesso", response.getBody());

        verify(produtoService, times(1)).deletar(id);
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

