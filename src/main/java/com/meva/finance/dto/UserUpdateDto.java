package com.meva.finance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
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




