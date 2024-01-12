package com.meva.finance.api;

import com.meva.finance.dto.UserDto;
import com.meva.finance.dto.UserUpdateDto;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.CpfNotFoundException;
import com.meva.finance.model.User;
import com.meva.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto) {
        try {
            User user = userService.saveUser(userDto);
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
        } catch (CpfExistingException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PutMapping("/update/{cpf}")
    @Transactional
    public ResponseEntity<?> updateUser(@PathVariable String cpf, @RequestBody UserUpdateDto updateUserDto) {
        try {
            User updatedUserDto = userService.updateUser(cpf, updateUserDto);
            return ResponseEntity.ok(String.format("Cpf %s atualizado com sucesso!", cpf));
        } catch (CpfNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/selectUser/{cpf}")
    @Transactional
    public ResponseEntity<?> selectUser(@PathVariable Long cpf) {
        try {
            User user = userService.selectUserById(cpf);
            return ResponseEntity.ok(user);
        } catch (CpfNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{cpf}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Long cpf) {
        try {
            userService.deleteUser(cpf);
            return ResponseEntity.ok("Usuário deletado com sucesso!");
        } catch (CpfNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}