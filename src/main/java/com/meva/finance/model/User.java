package com.meva.finance.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Entity
@Table(name = "user_meva")
public class User {

    @Id
    private String cpf;

    private String name;

    private String genre;

    private Date birth;

    private String state;

    private String city;

    @ManyToOne
    @JoinColumn(name = "id_family", referencedColumnName = "id_family")
    private Family family;
}
