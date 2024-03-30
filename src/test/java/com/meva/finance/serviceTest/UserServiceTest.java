package com.meva.finance.serviceTest;

import com.meva.finance.dto.FamilyDto;
import com.meva.finance.dto.UserDto;
import com.meva.finance.dto.UserUpdateDto;
import com.meva.finance.exception.CpfExistingException;
import com.meva.finance.exception.CpfNotFoundException;
import com.meva.finance.exception.IdFamilyNotFoundException;
import com.meva.finance.model.Family;
import com.meva.finance.model.User;
import com.meva.finance.repository.FamilyRepository;
import com.meva.finance.repository.UserRepository;
import com.meva.finance.service.UserService;
import org.junit.Before;
import org.junit.Test;
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
    private User testUser;
    private Family testFamily;
    private UserDto testUserDto;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FamilyRepository familyRepository;

    @InjectMocks
    private UserService userService;

    //TESTES DE SAVEUSER
    @Before
    public void setUp() {
        // Inicializa os mocks e o serviço de usuário
        userRepository = mock(UserRepository.class);
        familyRepository = mock(FamilyRepository.class);
        userService = new UserService(userRepository, familyRepository);

        // Cria uma família de teste
        testFamily = new Family();
        testFamily.setIdFamily(2L);
        testFamily.setDescription("Moraes");

        // Configura o mock para retornar a família de teste ao salvar
        when(familyRepository.save(any())).thenReturn(new Family());

        // Cria um DTO de família de teste
        FamilyDto testFamilyDto = new FamilyDto();
        testFamilyDto.setIdFamily(2L);
        testFamilyDto.setDescription("Moraes");

        // Cria um objeto Family a partir do FamilyDto
        Family testFamilyModel = testFamilyDto.converterFamily();

        // Configura o mock para retornar a família de teste ao buscar pelo ID
        when(familyRepository.findById(eq(2L))).thenReturn(Optional.of(testFamilyModel));

        // Cria um usuário de teste
        testUser = new User();
        testUser.setName("Gilson");
        testUser.setCpf("12345678900");
        testUser.setFamily(testFamily); // Associa o usuário à família de teste

        // Configura o mock para retornar o usuário de teste ao buscar pelo CPF
        when(userRepository.findByCpf(eq("12345678900"))).thenReturn(Optional.of(testUser));

        // Cria um DTO de usuário de teste
        testUserDto = new UserDto();
        testUserDto.setCpf("12345678900");
        testUserDto.setName("Gilson");
        testUserDto.setFamilyDto(new FamilyDto());
        testUserDto.getFamilyDto().setIdFamily(2L);
    }


    @Test
    @DisplayName("Testa usuário salvo com sucesso!")
    public void testSaveNewUserSuccess() {
        // Configuração do mock para o UserRepository
        when(userRepository.findByCpf(any(String.class))).thenReturn(Optional.empty());
        when(familyRepository.findById(any(Long.class))).thenReturn(Optional.of(testFamily));
        when(familyRepository.save(any())).thenReturn(new FamilyDto().converterFamily());

        // Configura o mock para retornar o usuário de teste ao salvar
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.saveUser(testUserDto);

        assertNotNull(savedUser);
        assertEquals(testUserDto.getName(), savedUser.getName());
    }

    @Test(expected = CpfExistingException.class)
    @DisplayName("Testa falha ao salvar usuário com CPF já existente")
    public void TestSaveNewUserWithCpfExistingException() throws CpfExistingException {
        when(userRepository.findByCpf(any(String.class))).thenReturn(Optional.of(new User()));

        userService.saveUser(testUserDto);

        // Verifica se o método save do userRepository nunca é chamado
        verify(userRepository, never()).save(any(User.class));
    }

    @Test(expected = IdFamilyNotFoundException.class)
    @DisplayName("Testa falha ao salvar usuário por família não encontrada")
    public void TestIdFamilyNotFoundException() throws IdFamilyNotFoundException {
        when(userRepository.findByCpf(any(String.class))).thenReturn(Optional.empty());
        when(familyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        userService.saveUser(testUserDto);
        verify(userRepository, never()).save(any(User.class));
    }

    //TESTES DE UPDATEUSER
    @Test
    @DisplayName("Testa usuário atualizado com sucesso")
    public void testUpdateUserSucess (){
        UserUpdateDto updateUserDto = new UserUpdateDto();
        updateUserDto.setName("Gilson Updated");

        User updatedUser = userService.updateUser("12345678900", updateUserDto);

        assertNotNull(updatedUser);
        assertEquals(updateUserDto.getName(), updatedUser.getName());
    }
    @Test
    @DisplayName("Testa falha na atualização do usuário por CPF ão encontrado")
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