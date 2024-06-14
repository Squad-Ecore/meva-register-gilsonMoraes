package com.meva.finance.apiTest;

import com.meva.finance.api.UserController;
import com.meva.finance.dto.UserDto;
import com.meva.finance.dto.UserUpdate;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.CpfNotFoundException;
import com.meva.finance.model.User;
import com.meva.finance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterUser_Success() throws CpfExistingException {
        UserDto userDto = new UserDto(/* preencha com dados válidos */);

        ResponseEntity<String> response = userController.registerUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário cadastrado com sucesso!", response.getBody());
    }

    @Test
    void testRegisterUser_Conflict() throws CpfExistingException {
        UserDto userDto = new UserDto(/* preencha com dados que causam conflito */);
        String cpf = "12345678900";
        when(userService.saveUser(userDto)).thenThrow(new CpfExistingException(cpf));

        ResponseEntity<String> response = userController.registerUser(userDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("O Cpf " + cpf + " já existe!", response.getBody());
    }

    @Test
    void testUpdateUser_Success() throws CpfNotFoundException {
        String cpf = "12345678900";
        UserUpdate updateUserDto = new UserUpdate(/* preencha com dados de atualização */);

        ResponseEntity<String> response = userController.updateUser(cpf, updateUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.format("Cpf %s atualizado com sucesso!", cpf), response.getBody());
    }

    @Test
    void testUpdateUser_NotFound() throws CpfNotFoundException {
        String cpf = "12345678900";
        UserUpdate updateUserDto = new UserUpdate(/* preencha com dados de atualização */);
        when(userService.updateUser(cpf, updateUserDto)).thenThrow
                (new CpfNotFoundException(cpf));

        ResponseEntity<String> response = userController.updateUser(cpf, updateUserDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("O Cpf " + cpf + " não foi encontrado!", response.getBody());
    }

    @Test
    void testSelectUser_Success() throws CpfNotFoundException {
        Long cpf = 12345678900L;
        User expectedUser = new User(/* preencha com dados esperados */);
        when(userService.selectUserById(cpf)).thenReturn(expectedUser);

        ResponseEntity<User> response = userController.selectUser(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
    }

    @Test
    void testSelectUser_NotFound() throws CpfNotFoundException {
        Long cpf = 12345678900L;
        when(userService.selectUserById(cpf)).thenThrow(new CpfNotFoundException("CPF não encontrado"));

        ResponseEntity<User> response = userController.selectUser(cpf);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteUser_Success() throws CpfNotFoundException {
        Long cpf = 12345678900L;

        ResponseEntity<String> response = userController.deleteUser(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário deletado com sucesso!", response.getBody());
    }

    @Test
    void testDeleteUser_NotFound() throws CpfNotFoundException {
        String cpf = "12345678900";
        doThrow(new CpfNotFoundException(cpf)).when(userService).deleteUser(cpf);

        ResponseEntity<String> response = userController.deleteUser(Long.valueOf((cpf)));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("O Cpf " + cpf + " não foi encontrado!", response.getBody());
    }
}
