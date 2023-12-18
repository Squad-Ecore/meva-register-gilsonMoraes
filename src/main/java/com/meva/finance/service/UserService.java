package com.meva.finance.service;

import com.meva.finance.dto.UserDto;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.IdFamilyNotFoundException;
import com.meva.finance.model.User;
import com.meva.finance.repository.FamilyRepository;
import com.meva.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    private FamilyRepository familyRepository;

    public UserService(UserRepository userRepository, FamilyRepository familyRepository) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    public User saveUser(UserDto userDto) {
        validateFamily(userDto);
        User user = userDto.converterUser();
        user.setFamily(userDto.getFamilyDto().converterFamily());
        familyRepository.save(user.getFamily());
        return userRepository.save(user);
        //converter userDto para user OK
        //salvar familia OK
    }

    //Criar método de validação

    public void validateFamily(UserDto userDto) throws CpfExistingException, IdFamilyNotFoundException {
        if (userRepository.findByCpf(userDto.getCpf()).isPresent()) {
            throw new CpfExistingException(userDto.getCpf());
        }
        if (familyRepository.findById(userDto.getFamilyDto().getIdFamily()).isPresent()) {
            throw new IdFamilyNotFoundException(userDto.getFamilyDto().getIdFamily());
        }
    }
}