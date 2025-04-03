package com.meva.finance.dto;

import com.meva.finance.model.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
public class UserDto {

    @CPF
    private String cpf;

    @NotBlank
    private String name;

    @Pattern(regexp = "(?i)[MF]", message = "Insira um gênero válido!")
    private String genre;

    @NotNull
    private LocalDate birth;

    @NotBlank
    private String state;

    @NotBlank
    private String city;

    private FamilyDto familyDto;

    public User converterUser() {
        User user = new User();
        user.setCpf(cpf);
        user.setName(name);
        user.setGenre(genre);
        user.setBirth(birth);
        user.setState(state);
        user.setCity(city);
        return user;
    }
}




