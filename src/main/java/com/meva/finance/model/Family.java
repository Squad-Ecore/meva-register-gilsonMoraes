package com.meva.finance.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "family")
public class Family {
    @Id
    @SequenceGenerator(name = "id_family", sequenceName = "id_family", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_family")
    @Column(name = "id_family")
    private Long idFamily;

    @NotNull
    @NotEmpty
    private String description;

}
