package com.meva.finance.dto;

import com.meva.finance.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;
import java.util.Date;

@Getter
@Setter
public class UserDto {

    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d{11}")
    private String cpf;

    @NotNull
    @NotEmpty
    private String name;

    @Pattern(regexp = "(?i)[MF]", message = "Insira um gênero válido!")
    private String genre;

    @NotNull
    private Date birth;

    @NotNull
    @NotEmpty
    private String state;

    @NotNull
    @NotEmpty
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
        return user;
    }
}




