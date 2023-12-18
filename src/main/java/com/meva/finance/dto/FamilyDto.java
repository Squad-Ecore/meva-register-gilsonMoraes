package com.meva.finance.dto;

import com.meva.finance.model.Family;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FamilyDto {
    @NotNull @NotEmpty
    private Long idFamily;

    @NotNull @NotEmpty
    private String description;

    public Family converterFamily(){
        Family family = new Family();
        family.setIdFamily(idFamily);
        family.setDescription(description);
        return family;
    }

}