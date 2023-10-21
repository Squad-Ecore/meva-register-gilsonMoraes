package com.meva.finance.service;

import com.meva.finance.dto.FamilyDto;
import com.meva.finance.dto.UserDto;
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

    public User register(UserDto userDto) {
        User user = userDto.converter();

        FamilyDto familyDto = userDto.getFamilyDto();

        if (familyDto.getIdFamily() == 0) {
            Family newFamily = new Family();
            newFamily.setDescription(familyDto.getDescription());

            // Salve a família no banco de dados para gerar um ID
            newFamily = familyRepository.save(newFamily);

            // Associe o usuário à família criada
            user.setFamily(newFamily);

        } else {
            // Tente encontrar a família no banco de dados pelo idFamily
            Family existingFamily = familyRepository.findById(familyDto.getIdFamily()).orElse(null);

            if (existingFamily != null) {
                // Se a família existir, associe o usuário a ela
                user.setFamily(existingFamily);
            } else {
                // Se a família não existe, crie uma nova família e associe o usuário a ela
                Family newFamily = new Family();
                newFamily.setDescription(familyDto.getDescription());

                // Salve a nova família no banco de dados
                newFamily = familyRepository.save(newFamily);

                // Associe o usuário à nova família
                user.setFamily(newFamily);
            }
        }
        // Salve o usuário no banco de dados
        return userRepository.save(user);
    }
}

