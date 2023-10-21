package com.meva.finance.repository;

import com.meva.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//isso permite que eu realize operações de CRUD utilizando metodos fornecidos pelo JPARepository

public interface UserRepository extends JpaRepository<User, String> {
}
