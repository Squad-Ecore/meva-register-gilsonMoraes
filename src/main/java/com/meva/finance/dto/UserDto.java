package com.meva.finance.dto;

import com.meva.finance.model.User;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UserDto {

    @CPF
    private String cpf;

    @NotBlank
    private String name;

    @Pattern(regexp = "(?i)[MF]", message = "Insira um gênero válido!")
    private String genre;

    @NotBlank
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
        //Chamar converter familyDto para setar na family que está dentro de user -- OK
        //aprender sobre BUILDER e refatorar
        return user;
    }
}




