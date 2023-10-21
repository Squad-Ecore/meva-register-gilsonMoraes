package com.meva.finance.api;

import com.meva.finance.dto.UserDto;
import com.meva.finance.model.User;
import com.meva.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;


@RestController // Esse controle é um rest controller
@RequestMapping("/users") // < MAPEAMENTO
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // Injeção de dependência

    @PostMapping("/register")
    @Transactional
    public User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.register(userDto);
    }
}
