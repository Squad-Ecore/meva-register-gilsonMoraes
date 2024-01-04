package com.meva.finance.service;

import com.meva.finance.dto.UserDto;
import com.meva.finance.dto.UserUpdateDto;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.IdFamilyNotFoundException;
import com.meva.finance.model.Family;
import com.meva.finance.model.User;
import com.meva.finance.repository.FamilyRepository;
import com.meva.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final FamilyRepository familyRepository;

    public UserService(UserRepository userRepository, FamilyRepository familyRepository) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    public User saveUser(UserDto userDto) {
        User user = userDto.converterUser();
        user.setFamily(validateFamily(userDto));
        return userRepository.save(user);
    }

    private Family validateFamily(UserDto userDto)
            throws CpfExistingException, IdFamilyNotFoundException {
        if (userRepository.findByCpf(userDto.getCpf()).isPresent()) {
            throw new CpfExistingException(userDto.getCpf());
        }
        if (familyRepository.findById(userDto.getFamilyDto().getIdFamily()).isPresent() ||
                (userDto.getFamilyDto().getIdFamily() == null || userDto.getFamilyDto().getIdFamily() == 0)) {
            return familyRepository.save(userDto.getFamilyDto().converterFamily());
        }
        throw new IdFamilyNotFoundException(userDto.getFamilyDto().getIdFamily());
    }

    public User updateUser(Long userId, UserUpdateDto updateUserDto)
            throws CpfExistingException, IdFamilyNotFoundException {
        User existingUser = userRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new CpfExistingException(updateUserDto.getCpf()));

        if (!familyRepository.findById(updateUserDto.getFamilyDto().getIdFamily()).isPresent()){
            throw new IdFamilyNotFoundException(updateUserDto.getFamilyDto().getIdFamily());
        }

        updateFields(existingUser, updateUserDto);

        return userRepository.save(existingUser);
    }

    private void updateFields(User existingUser, UserUpdateDto updateUserDto) {
        if (updateUserDto.getName() != null && !updateUserDto.getName().isEmpty()) {
            existingUser.setName(updateUserDto.getName().toUpperCase());
        }

        if (updateUserDto.getGenre() != null && !updateUserDto.getGenre().isEmpty()) {
            existingUser.setGenre(updateUserDto.getGenre().toUpperCase());
        }

        if (updateUserDto.getState() != null && !updateUserDto.getState().isEmpty()) {
            existingUser.setState(updateUserDto.getState().toUpperCase());
        }

        if (updateUserDto.getCity() != null && !updateUserDto.getCity().isEmpty()) {
            existingUser.setCity(updateUserDto.getCity().toUpperCase());
        }

        Date birth = updateUserDto.getBirth();
        if (birth != null) {
            existingUser.setBirth(birth);
        }
    }
}