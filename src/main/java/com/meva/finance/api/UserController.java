package com.meva.finance.api;

import com.meva.finance.dto.UserDto;
import com.meva.finance.dto.UserUpdateDto;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.IdFamilyNotFoundException;
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
    public User register(@RequestBody @Valid UserDto userDto)
            throws CpfExistingException, IdFamilyNotFoundException {
        return userService.saveUser(userDto);
    }

    @PutMapping("/update/{userId}")
    @Transactional
    public User updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateDto updateUserDto)
            throws CpfExistingException, IdFamilyNotFoundException {
        return userService.updateUser(Long.valueOf(userId), updateUserDto);
    }
}