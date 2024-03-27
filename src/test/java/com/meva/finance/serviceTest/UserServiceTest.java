package com.meva.finance.serviceTest;

import com.meva.finance.dto.FamilyDto;
import com.meva.finance.dto.UserDto;
import com.meva.finance.dto.UserUpdateDto;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.CpfNotFoundException;
import com.meva.finance.exception.IdFamilyNotFoundException;
import com.meva.finance.model.User;
import com.meva.finance.repository.FamilyRepository;
import com.meva.finance.repository.UserRepository;
import com.meva.finance.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FamilyRepository familyRepository;

    @InjectMocks
    private UserService userService;

    @Before
    public void startBefore() {
        MockitoAnnotations.initMocks(this);
    }


    //TESTES DE SAVEUSER
    @Test
    @DisplayName("Testa usuário salvo com sucesso!")
    public void TestSaveNewUserSucess() {
        // Configuração do mock para o UserRepository
        when(userRepository.findByCpf(any(String.class))).thenReturn(Optional.empty());
        when(familyRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(familyRepository.save(any())).thenReturn(new FamilyDto().converterFamily());

        // Configura o mock para retornar um usuário simulado ao salvar
        User userTest = new User();
        userTest.setName("Gilson");
        when(userRepository.save(any(User.class))).thenReturn(userTest);

        // Dados de entrada para o método saveUsers
        UserDto userDto = new UserDto();
        userDto.setCpf("12345678900"); // CPF que não existe no banco de dados
        userDto.setName("Gilson");
        userDto.setFamilyDto(new FamilyDto());

        // Chamada do método saveUser
        User savedUser = userService.saveUser(userDto);

        // Verifica se o usuário foi salvo corretamente
        assertNotNull(savedUser);
        assertEquals(userDto.getName(), savedUser.getName());
        // Inclua mais verificações conforme necessário para outros campos
    }

    @Test(expected = CpfExistingException.class)
    public void TestSaveNewUserWithCpfExistingException() throws CpfExistingException {
        // Configura o mock para retornar um usuário existente com o mesmo CPF
        when(userRepository.findByCpf(any(String.class))).thenReturn(Optional.of(new User()));

        // Dados de entrada para o método saveUser com CPF duplicado
        UserDto userDto = new UserDto();
        userDto.setCpf("12345678900"); // CPF que já existe no banco de dados

        // Chamada do método saveUser, que deve lançar uma exceção
        userService.saveUser(userDto);

        // Verifica se o método save do userRepository nunca é chamado
        verify(userRepository, never()).save(any(User.class));
    }

    @Test(expected = IdFamilyNotFoundException.class)
    public void TestIdFamilyNotFoundException() throws IdFamilyNotFoundException {
        when(userRepository.findByCpf(any(String.class))).thenReturn(Optional.empty());
        when(familyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        UserDto userDto = new UserDto();
        userDto.setFamilyDto(new FamilyDto());
        userDto.getFamilyDto().setIdFamily(2L);

        userService.saveUser(userDto);
    }

    //TESTES DE UPDATEUSER

//    @Test
//    public void testUpdateUserSuccess() {
//        // Definindo dados de teste
//        String cpf = "12345678900";
//        long familyId = 1L;
//        UserUpdateDto updateUserDto = new UserUpdateDto();
//        FamilyDto familyDto = new FamilyDto();
//        familyDto.setIdFamily(familyId);
//        updateUserDto.setFamilyDto(familyDto);
//
//        // Criando usuário existente
//        User existingUser = new User();
//        existingUser.setCpf(cpf);
//
//        // Criando uma instância de Family
//        Family family = new Family();
//        family.setIdFamily(familyId);
//
//        // Configurando comportamento dos mocks
//        when(userRepository.findById(cpf)).thenReturn(Optional.of(existingUser));
//        when(familyRepository.findById(familyId)).thenReturn(Optional.of(family));
//
//        // Chamando o método sob teste
//        User updatedUser = userService.updateUser(cpf, updateUserDto);
//
//        // Verificando se o usuário foi atualizado corretamente
//        assertEquals(family, updatedUser.getFamily());
//        verify(userRepository, times(1)).findById(cpf);
//        verify(familyRepository, times(1)).findById(familyId);
//        verify(userRepository, times(1)).save(existingUser);
//    }
    @Test
    public void testUpdateUserCpfNotFoundException() {
        UserRepository userRepository = mock(UserRepository.class);
        FamilyRepository familyRepository = mock(FamilyRepository.class);
        UserService userService = new UserService(userRepository, familyRepository);

        String cpf = "12345678900";
        UserUpdateDto updateUserDto = new UserUpdateDto();
        updateUserDto.setFamilyDto(new FamilyDto());

        when(userRepository.findById(cpf)).thenReturn(Optional.empty());

        assertThrows(CpfNotFoundException.class, () -> userService.updateUser(cpf, updateUserDto));
    }

    @Test
    public void testUpdateUserIdFamilyNotFoundException() {
        UserRepository userRepository = mock(UserRepository.class);
        FamilyRepository familyRepository = mock(FamilyRepository.class);
        UserService userService = new UserService(userRepository, familyRepository);

        String cpf = "12345678900";
        UserUpdateDto updateUserDto = new UserUpdateDto();
        FamilyDto familyDto = new FamilyDto();
        familyDto.setIdFamily(1L);
        updateUserDto.setFamilyDto(familyDto);

        User existingUser = new User();
        existingUser.setCpf(cpf);

        when(userRepository.findById(cpf)).thenReturn(Optional.of(existingUser));
        when(familyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdFamilyNotFoundException.class, () -> userService.updateUser(cpf, updateUserDto));
    }
}