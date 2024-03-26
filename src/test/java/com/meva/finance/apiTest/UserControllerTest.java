package com.meva.finance.apiTest;

import com.meva.finance.api.UserController;
import com.meva.finance.dto.UserDto;
import com.meva.finance.model.User;
import com.meva.finance.service.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    // Inicializa os mocks e injeta na UserController
    public UserControllerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterUser_Success(){

        when(userService.saveUser(any(UserDto.class))).thenReturn(new User());
        ResponseEntity<?> responseEntity = userController.registerUser(new UserDto());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Usuário cadastrado com sucesso!", responseEntity.getBody());

    }

}
