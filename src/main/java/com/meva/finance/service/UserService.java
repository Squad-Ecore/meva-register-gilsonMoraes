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

    private Family createFamily(FamilyDto familyDto, User user) {
        Family newFamily = new Family();
        newFamily.setDescription(familyDto.getDescription());
        newFamily = familyRepository.save(newFamily);
        user.setFamily(newFamily);
        return newFamily;
    }

    public User register(UserDto userDto) {
        User user = userDto.converter();
        FamilyDto familyDto = userDto.getFamilyDto();
        if (familyDto.getIdFamily() == 0) {
            createFamily(familyDto, user);
        } else {
            Family existingFamily = familyRepository.findById(familyDto.getIdFamily()).orElse(null);
            if (existingFamily != null) {
                user.setFamily(existingFamily);
            } else {
                createFamily(familyDto, user);
            }
        }
        return userRepository.save(user);
    }

    public User updateUser(String cpf, UserDto updatedUserDto) {
        User existingUser = userRepository.findById(cpf).orElse(null);
        if (existingUser != null) {
            existingUser.setName(updatedUserDto.getName());
            existingUser.setGenre(updatedUserDto.getGenre());
            existingUser.setBirth(updatedUserDto.getBirth());
            existingUser.setState(updatedUserDto.getState());
            existingUser.setCity(updatedUserDto.getCity());
            return userRepository.save(existingUser);
        } else {
            // O usuário não foi encontrado, você pode lidar com isso de acordo com sua lógica de negócios
            return null;
        }
    }
}


