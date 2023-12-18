package com.meva.finance.service;

import com.meva.finance.dto.UserDto;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.IdFamilyNotFoundException;
import com.meva.finance.model.Family;
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
        User user = userDto.converterUser();
        user.setFamily(validateFamily(userDto));
        return userRepository.save(user);
    }

    private Family validateFamily(UserDto userDto) throws CpfExistingException, IdFamilyNotFoundException {
        if (userRepository.findByCpf(userDto.getCpf()).isPresent()) {
            throw new CpfExistingException(userDto.getCpf());
        }
        if (familyRepository.findById(userDto.getFamilyDto().getIdFamily()).isPresent() ||
                (userDto.getFamilyDto().getIdFamily() == null || userDto.getFamilyDto().getIdFamily() == 0)) {
            Family family = familyRepository.save(userDto.getFamilyDto().converterFamily());
            return family;
        }
        throw new IdFamilyNotFoundException(userDto.getFamilyDto().getIdFamily());
    }
}