package com.meva.finance.service;

import com.meva.finance.dto.UserDto;
import com.meva.finance.dto.UserUpdate;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.CpfNotFoundException;
import com.meva.finance.exception.IdFamilyNotFoundException;
import com.meva.finance.model.Family;
import com.meva.finance.model.User;
import com.meva.finance.repository.FamilyRepository;
import com.meva.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

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

    public User updateUser(String cpf, UserUpdate updateUserDto)
            throws CpfNotFoundException, IdFamilyNotFoundException {
        User existingUser = userRepository.findById(cpf)
                .orElseThrow(() -> new CpfNotFoundException(cpf));

        if (!familyRepository.findById(updateUserDto.getFamilyDto().getIdFamily()).isPresent()) {
            throw new IdFamilyNotFoundException(updateUserDto.getFamilyDto().getIdFamily());
        }

        updateFields(existingUser, updateUserDto);

        return userRepository.save(existingUser);
    }

    private void updateFields(User existingUser, UserUpdate updateUserDto) {
        existingUser.setName(getUpdatedValue(existingUser.getName(), updateUserDto.getName()));
        existingUser.setGenre(getUpdatedValue(existingUser.getGenre(), updateUserDto.getGenre()));
        existingUser.setState(getUpdatedValue(existingUser.getState(), updateUserDto.getState()));
        existingUser.setCity(getUpdatedValue(existingUser.getCity(), updateUserDto.getCity()));

        LocalDate birth = updateUserDto.getBirth();
        if (birth != null) {
            existingUser.setBirth(birth);
        }
    }

    private String getUpdatedValue(String currentValue, String newValue) {
        return (newValue != null && !newValue.isEmpty()) ? newValue : currentValue;
    }

    public User selectUserById(Long cpf) throws CpfNotFoundException {
        Optional<User> userOptional = userRepository.findById(String.valueOf(cpf));

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new CpfNotFoundException(String.valueOf(cpf));
        }
    }

    public void deleteUser(Long cpf) throws CpfNotFoundException {
        Optional<User> userOptional = userRepository.findById(String.valueOf(cpf));

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        } else {
            throw new CpfNotFoundException(String.valueOf(cpf));
        }
    }
}