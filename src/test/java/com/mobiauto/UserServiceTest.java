package com.mobiauto;

import com.mobiauto.data.User;
import com.mobiauto.service.UserService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@MicronautTest
public class UserServiceTest {

    @Inject
    UserService userRepository; // Injetando o repositório real

    @Inject
    UserService userService; // Injetando o serviço real

    @Test
    void testFindUserById() {
        // Define o comportamento do mock quando o método findById é chamado com um ID específico
        when(userRepository.getUserById(1L)).thenReturn(new User( "john_doe"));

        // Chama o método do serviço que busca um usuário pelo ID
        User user = userService.getUserById(1L);

        // Verifica se o usuário retornado é o esperado
        assertEquals("john_doe", user.getUsername());
    }

    @Test
    void testFindUserByUsername() {
        // Define o comportamento do mock quando o método findByUsername é chamado com um nome de usuário específico
        when(userRepository.getUserByUsername("jane_smith")).thenReturn(new User("jane_smith"));

        // Chama o método do serviço que busca um usuário pelo nome de usuário
        User user = userService.getUserByUsername("jane_smith");

        // Verifica se o usuário retornado é o esperado
        assertEquals(2L, user.getId());
    }

    @Test
    void testSaveUser() {
        // Cria um novo usuário para salvar
        User newUser = new User( "new_user");

        // Define o comportamento do mock quando o método save é chamado
        when(userRepository.createUser(newUser)).thenReturn(new User("new_user"));

        // Chama o método do serviço que salva um novo usuário
        User savedUser = userService.createUser(newUser);

        // Verifica se o usuário salvo tem um ID atribuído
        assertEquals(3L, savedUser.getId());
    }
}
