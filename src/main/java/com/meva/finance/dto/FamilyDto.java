package com.meva.finance.dto;

import com.meva.finance.model.Family;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class FamilyDto {
    @NotNull
    @NotEmpty
    private Long idFamily;

    @NotNull
    @NotEmpty
    private String description;

    public Family converterFamily() {
        Family family = new Family();
        family.setIdFamily(idFamily);
        family.setDescription(description);
        return family;
    }
}