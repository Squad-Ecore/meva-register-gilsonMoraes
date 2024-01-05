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
import java.util.function.Consumer;

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

        if (!familyRepository.findById(updateUserDto.getFamilyDto().getIdFamily()).isPresent()) {
            throw new IdFamilyNotFoundException(updateUserDto.getFamilyDto().getIdFamily());
        }

        updateField(existingUser, updateUserDto.getName(), User::setName);
        updateField(existingUser, updateUserDto.getGenre(), User::setGenre);
        updateField(existingUser, updateUserDto.getState(), User::setState);
        updateField(existingUser, updateUserDto.getCity(), s -> User::setCity);

        updateBirth(existingUser, updateUserDto.getBirth());

        return userRepository.save(existingUser);
    }

    private void updateField(User existingUser, String value, Consumer<String> setter) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value.toUpperCase());
        }
    }

    private void updateBirth(User existingUser, Date birth) {
        if (birth != null) {
            existingUser.setBirth(birth);
        }
    }
}
