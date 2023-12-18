package com.meva.finance.repository;

import com.meva.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//isso permite que eu realize operações de CRUD utilizando metodos fornecidos pelo JPARepository
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByCpf(String cpf);
}
