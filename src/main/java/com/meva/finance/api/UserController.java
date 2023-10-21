package com.meva.finance.api;

import com.meva.finance.dto.UserDto;
import com.meva.finance.model.User;
import com.meva.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;


@RestController // Esse controle é um rest controller
@RequestMapping("/users") // < MAPEAMENTO
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // Injeção de dependência

    @PostMapping("/register")
    @Transactional
    public User register(@RequestBody @Valid UserDto userDto) {
        return userService.register(userDto);
    }

    @PutMapping("/update/{cpf}")
    public User updateUserByCpf(@PathVariable String cpf, @RequestBody UserDto updatedUserDto) {
        User updatedUser = userService.updateUser(cpf, updatedUserDto);
        if (updatedUser != null) {
        }
        return updatedUser;
    }
}