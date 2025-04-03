package com.meva.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_meva")
public class User {

    @Id
    private String cpf;
    private String name;
    private String genre;
    private LocalDate birth;
    private String state;
    private String city;

    @ManyToOne
    @JoinColumn(name = "id_family", referencedColumnName = "id_family")
    private Family family;
}
