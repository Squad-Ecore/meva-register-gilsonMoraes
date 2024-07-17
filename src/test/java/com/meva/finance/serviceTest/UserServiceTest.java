package com.meva.finance.serviceTest;

import com.meva.finance.dto.FamilyDto;
import com.meva.finance.dto.UserDto;
import com.meva.finance.dto.UserUpdate;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.CpfNotFoundException;
import com.meva.finance.exception.IdFamilyNotFoundException;
import com.meva.finance.model.Family;
import com.meva.finance.model.User;
import com.meva.finance.repository.FamilyRepository;
import com.meva.finance.repository.UserRepository;
import com.meva.finance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private User testUser;
    private Family testFamily;
    private UserDto testUserDto;
    private FamilyDto testFamilyDto;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FamilyRepository familyRepository;

    @InjectMocks
    private UserService userService;

    //TESTES DE SAVEUSER
    @BeforeEach
    public void setUp() {
        //DTO PARA TESTAR FAMÍLIA
        testFamilyDto = new FamilyDto();
        testFamilyDto.setIdFamily(1L);
        testFamilyDto.setDescription("Familia Dto");

        //DTO PARA TESTAR USUÁRIO
        testUserDto = new UserDto();
        testUserDto.setCpf("12345678900");
        testUserDto.setName("Nome Dto");
        testUserDto.setGenre("M");
        testUserDto.setBirth(LocalDate.parse("1990-02-02"));
        testUserDto.setState("Rio de janeiro Dto");
        testUserDto.setCity("Nova iguaçu Dto");
        testUserDto.setFamilyDto(testFamilyDto);

        //TESTE PARA TESTAR FAMÍLIA
        testFamily = new Family();
        testFamily.setIdFamily(1L);
        testFamily.setDescription("Familia");

        //TESTE PARA TESTAR USUÁRIO
        testUser = new User();
        testUser.setCpf("12345678911");
        testUser.setName("Nome");
        testUser.setGenre("M");
        testUser.setBirth(LocalDate.parse("1990-02-02"));
        testUser.setState("Rio de janeiro");
        testUser.setCity("Nova iguaçu");
        testUser.setFamily(testFamily);
    }

    @Test // OK
    @DisplayName("Usuário salvo com sucesso!")
    public void testSaveUser() {
        when(userRepository.findByCpf(testUserDto.getCpf())).thenReturn(Optional.empty());
        when(familyRepository.findById(testUserDto.getFamilyDto().getIdFamily())).thenReturn(Optional.of(testFamily));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(familyRepository.save(any(Family.class))).thenReturn(testFamily);

        User savedUser = userService.saveUser(testUserDto);

        assertEquals(testUser.getCpf(), savedUser.getCpf());
        assertNotNull(savedUser.getFamily());

        verify(userRepository, times(1)).save(any(User.class));
        verify(familyRepository, times(1)).save(any(Family.class));
    }

    @Test // OK
    @DisplayName("Salvar usuário com CPF existente")
    public void testSaveUserCpfExistingException() {
        when(userRepository.findByCpf(testUserDto.getCpf())).thenReturn(Optional.of(testUser));

        assertThrows(CpfExistingException.class, () -> userService.saveUser(testUserDto));

        verify(userRepository, times(0)).save(any(User.class));
        verify(familyRepository, times(0)).save(any(Family.class));
    }

    @Test // OK
    @DisplayName("Salvar usuário com família inexistente")
    public void testSaveUserIdFamilyNotFoundException() throws IdFamilyNotFoundException {
        when(userRepository.findByCpf(testUserDto.getCpf())).thenReturn(Optional.empty());
        when(familyRepository.findById(testUserDto.getFamilyDto().getIdFamily())).thenReturn(Optional.empty());

        assertThrows(IdFamilyNotFoundException.class, () -> userService.saveUser(testUserDto));

        verify(userRepository, times(0)).save(any(User.class));
        verify(familyRepository, times(0)).save(any(Family.class));
    }


    //TESTES DE UPDATEUSER
    @Test
    @DisplayName("Atualização de usuário com sucesso")
    public void testUpdateUserSuccess() {
        String cpf = testUser.getCpf();
        UserUpdate updateUserDto = new UserUpdate();
        updateUserDto.setName("Novo Nome");
        updateUserDto.setCity("Nova Cidade");
        updateUserDto.setState("Novo Estado");
        updateUserDto.setFamilyDto(testFamilyDto);

        when(userRepository.findByCpf(cpf)).thenReturn(Optional.of(testUser));
        when(familyRepository.findById(testFamilyDto.getIdFamily()))
                .thenReturn(Optional.of(testFamily));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedUser = userService.updateUser(cpf, updateUserDto);

        assertEquals(updateUserDto.getName(), updatedUser.getName());
        assertEquals(updateUserDto.getCity(), updatedUser.getCity());
        assertEquals(updateUserDto.getState(), updatedUser.getState());
        assertNotNull(updatedUser.getFamily());
        assertEquals(testFamily.getIdFamily(), updatedUser.getFamily().getIdFamily());

        verify(userRepository, times(1)).findByCpf(cpf);
        verify(familyRepository, times(1)).findById(testFamilyDto.getIdFamily());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("CPF não encontrado na atualização de usuário")
    public void testUpdateUserCpfNotFoundException() {
        String cpf = "99999999999"; // CPF que nao existe no bd
        UserUpdate updateUserDto = new UserUpdate();
        updateUserDto.setFamilyDto(testFamilyDto);

        when(userRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(CpfNotFoundException.class, () -> userService.updateUser(cpf, updateUserDto));


        verify(userRepository, times(1)).findByCpf(cpf);
        verify(familyRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("ID de Família não encontrado na atualização de usuário")
    public void testUpdateUserIdFamilyNotFoundException() {
        String cpf = testUser.getCpf();
        UserUpdate updateUserDto = new UserUpdate();
        updateUserDto.setFamilyDto(testFamilyDto);

        when(userRepository.findByCpf(cpf)).thenReturn(Optional.of(testUser));
        when(familyRepository.findById(testFamilyDto.getIdFamily()))
                .thenReturn(Optional.empty());

        assertThrows(IdFamilyNotFoundException.class,
                () -> userService.updateUser(cpf, updateUserDto));

        verify(userRepository, times(1)).findByCpf(cpf);
        verify(familyRepository, times(1)).findById(testFamilyDto.getIdFamily());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("Selecionar usuário por CPF com sucesso!")
    public void testSelectUserByCpfNotFoundException() {
        when(userRepository.findByCpf(String.valueOf(testUser.getCpf())))
                .thenReturn(Optional.of(testUser));

        User selectedUser = userService.selectUserById(Long.valueOf(testUser.getCpf()));

        assertNotNull(selectedUser);
        assertEquals(testUser.getCpf(), selectedUser.getCpf());
        assertEquals(testUser.getName(), selectedUser.getName());

        verify(userRepository, times(1))
                .findByCpf(String.valueOf(testUser.getCpf()));
    }

    @Test
    @DisplayName("Falha ao encontrar usuário pelo Cpf!")
    public void testSelectUserByIdUserNotFound() {
        when(userRepository.findByCpf(String.valueOf(testUser.getCpf())))
                .thenReturn(Optional.empty());

        assertThrows(CpfNotFoundException.class,
                () -> userService.selectUserById(Long.valueOf(testUser.getCpf())));

        verify(userRepository, times(1))
                .findByCpf(String.valueOf(testUser.getCpf()));
    }

    @Test
    @DisplayName("Deleta usuário com sucesso")
    public void testDeleteUserSucess() throws CpfNotFoundException {
        when(userRepository.findByCpf(testUser.getCpf()))
                .thenReturn(Optional.of(testUser));

        assertDoesNotThrow(() -> userService.deleteUser(testUser.getCpf()));

        verify(userRepository, times(1))
                .deleteById(testUser.getCpf());
    }

    @Test
    @DisplayName("Falha ao deletar usuário!")
    public void testDeleteUserByCpfNotFoundException(){
        when(userRepository.findByCpf(testUser.getCpf()))
                .thenReturn(Optional.empty());

        assertThrows(CpfNotFoundException.class,
                () -> userService.deleteUser(testUser.getCpf()));

        verify(userRepository, times(0)).deleteById(any());
    }
}