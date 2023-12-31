package com.meva.finance.service;

import com.meva.finance.dto.UserDto;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.CpfNotFoundException;
import com.meva.finance.exception.IdFamilyNotFoundException;
import com.meva.finance.model.Family;
import com.meva.finance.model.User;
import com.meva.finance.repository.FamilyRepository;
import com.meva.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User updateUser(Long userId, UserDto updateUserDto)
            throws CpfExistingException, IdFamilyNotFoundException {

        // Busca o usuário existente no banco de dados
        Optional<User> existingUserOptional = userRepository.findById(String.valueOf(userId));

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Atualiza apenas os campos desejados do usuário existence
            if (updateUserDto.getName() != null) {
                existingUser.setName(updateUserDto.getName());
            }
            if (updateUserDto.getGenre() != null) {
                existingUser.setGenre(updateUserDto.getGenre());
            }
            // Adicione condições semelhantes para outros campos que podem ser atualizados

            // Validação e atualização da família
            Family updatedFamily = validateFamily(updateUserDto);
            existingUser.setFamily(updatedFamily);

            // Salva o usuário atualizado
            return userRepository.save(existingUser);
        } else {
            // Lidar com o caso em que o usuário não existe
            throw new CpfExistingException(updateUserDto.getCpf());
        }
    }

    private Family validateFamily(UserDto userDto)
            throws CpfExistingException, IdFamilyNotFoundException {
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