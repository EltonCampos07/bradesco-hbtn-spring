package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveRetornarUsuarioQuandoIdExistir() {
        // Arrange
        Long id = 1L;
        Usuario usuarioEsperado = new Usuario(id, "João Silva", "joao@example.com");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEsperado));

        // Act
        Usuario usuarioRetornado = usuarioService.buscarUsuarioPorId(id);

        // Assert
        assertNotNull(usuarioRetornado);
        assertEquals(id, usuarioRetornado.getId());
        assertEquals("João Silva", usuarioRetornado.getNome());
        assertEquals("joao@example.com", usuarioRetornado.getEmail());
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        Long id = 99L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarUsuarioPorId(id);
        });

        assertEquals("Usuário não encontrado com id: " + id, exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    public void deveSalvarUsuarioComSucesso() {
        // Arrange
        Usuario usuarioParaSalvar = new Usuario(null, "Maria Santos", "maria@example.com");
        Usuario usuarioSalvo = new Usuario(2L, "Maria Santos", "maria@example.com");
        when(usuarioRepository.save(usuarioParaSalvar)).thenReturn(usuarioSalvo);

        // Act
        Usuario usuarioRetornado = usuarioService.salvarUsuario(usuarioParaSalvar);

        // Assert
        assertNotNull(usuarioRetornado);
        assertEquals(2L, usuarioRetornado.getId());
        assertEquals("Maria Santos", usuarioRetornado.getNome());
        assertEquals("maria@example.com", usuarioRetornado.getEmail());
        verify(usuarioRepository, times(1)).save(usuarioParaSalvar);
    }
}

