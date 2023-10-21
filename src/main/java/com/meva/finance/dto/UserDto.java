package com.meva.finance.dto;

import com.meva.finance.model.Family;
import com.meva.finance.model.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.Date;

@Getter
@Setter
public class UserDto {

    @NotEmpty
    @Pattern(regexp = "\\d{11}")
    private String cpf;

    @NotEmpty
    private String name;

    private String genre;

    @NotNull
    private Date birth;

    @NotEmpty
    private String state;

    @NotEmpty
    private String city;

    private FamilyDto familyDto;

    public User converter() {
        User user = new User();
        user.setCpf(cpf);
        user.setName(name);
        user.setGenre(genre);
        user.setBirth(birth);
        user.setState(state);
        user.setCity(city);
        Family family = familyDto.converter();
        user.setFamily(family);
        //Chamar converter familyDto para setar na family que está dentro de user -- OK
        return user;
    }

}


