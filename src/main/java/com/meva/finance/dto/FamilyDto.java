package com.meva.finance.dto;

import com.meva.finance.model.Family;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FamilyDto {
    private Long idFamily;

    @NotNull
    private String description;

    public Family converter(){
        Family family = new Family();
        family.setIdFamily(idFamily);
        family.setDescription(description);
        return family;
    }

}