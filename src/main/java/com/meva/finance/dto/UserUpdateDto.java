package com.meva.finance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserUpdateDto {

    @JsonIgnore
    private String cpf;

    private String name;

    private String genre;

    private Date birth;

    private String state;

    private String city;

    private FamilyDto familyDto;
}
