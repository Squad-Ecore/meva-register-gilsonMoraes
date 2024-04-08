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
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
    private FamilyDto testFamilyDto;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FamilyRepository familyRepository;

    @InjectMocks
    private UserService userService;

    //TESTES DE SAVEUSER
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Criação da família de teste e configuração do mock para retornar a família de teste ao buscar pelo ID
        testFamily = new Family();
        testFamily.setIdFamily(2L);
        testFamily.setDescription("Moraes");
        new FamilyDto().converterFamily();

        testFamilyDto = new FamilyDto();
        testFamilyDto.setIdFamily(3L);
        testFamilyDto.setDescription("Rogick");
        new FamilyDto().converterFamily();

        // Criação do usuário de teste e configuração do mock para retornar o usuário de teste ao buscar pelo CPF
        testUser = new User();
        testUser.setCpf("12345678900");
        testUser.setName("Gilson");
        testUser.setGenre("M");
        testUser.setBirth(LocalDate.parse("2222-12-22"));
        testUser.setState("Rio de Janeiro");
        testUser.setCity("Nova Iguaçu");
        testUser.setFamily(testFamily);

        // Criação do DTO de usuário de teste
        testUserDto = new UserDto();
        testUserDto.setCpf("12345678900");
        testUserDto.setName("Gilson");
        testUserDto.setGenre("M");
        testUserDto.setBirth(LocalDate.parse("2222-12-22"));
        testUserDto.setState("Rio de Janeiro");
        testUserDto.setCity("Nova Iguaçu");
        testUserDto.setFamilyDto(new FamilyDto());
        testUserDto.getFamilyDto().setIdFamily(3L);
    }


    @Test
    @DisplayName("Testa usuário salvo com sucesso!")
    public void testSaveNewUserSuccess() {
        // Configuração do mock para o UserRepository
        when(userRepository.findByCpf(any(String.class))).thenReturn(Optional.empty());
        when(familyRepository.findById(3L)).thenReturn(Optional.of(testFamily));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Execução do método a ser testado
        User savedUser = userService.saveUser(testUserDto);

        // Verificação do resultado
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
    @DisplayName("Testa atualização de usuário com sucesso")
    public void testUpdateUserSuccess() {
        when(userRepository.findById("12345678900")).thenReturn(Optional.of(testUser));
        when(familyRepository.findById(3L)).thenReturn(Optional.of(testFamily));

        UserUpdate updatedUserDto = new UserUpdate();
        updatedUserDto.setCpf("12345678900");
        updatedUserDto.setName("Carlos");
        updatedUserDto.setFamilyDto(testFamilyDto);

        // Chama o método a ser testado, passando o CPF e o objeto UserUpdateDto
        User updatedUser = userService.updateUser("12345678900", updatedUserDto);

        // Verifica se o usuário foi atualizado corretamente
        assertNotNull(updatedUser);
        assertEquals("Carlos", updatedUser.getName());
    }

    @Test
    @DisplayName("Testa falha na atualização do usuário por CPF não encontrado")
    public void testUpdateUserCpfNotFoundException() {
        String cpf = "12345678900";
        UserUpdate updateUserDto = new UserUpdate();
        updateUserDto.setFamilyDto(new FamilyDto());

        when(userRepository.findById(cpf)).thenReturn(Optional.empty());

        assertThrows(CpfNotFoundException.class, () -> userService.updateUser(cpf, updateUserDto)); // estudar isso
    }

    @Test
    public void testUpdateUserIdFamilyNotFoundException() {
        String cpf = "12345678900";
        UserUpdate updateUserDto = new UserUpdate();
        FamilyDto familyDto = new FamilyDto();
        familyDto.setIdFamily(1L);
        updateUserDto.setFamilyDto(familyDto);

        User existingUser = new User();
        existingUser.setCpf(cpf);

        when(userRepository.findById(cpf)).thenReturn(Optional.of(existingUser));
        when(familyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdFamilyNotFoundException.class, () -> userService.updateUser(cpf, updateUserDto));
    }

    @Test
    @DisplayName("Testa selecionar usuário por CPF com sucesso!")
    public void selectUserSuccessByCpf() {
        // Preparação
        String cpf = "12345678900"; // Define o CPF de teste
        when(userRepository.findById(cpf)).thenReturn(Optional.of(testUser)); // Configura o mock para retornar o usuário de teste quando buscar pelo CPF

        // Execução
        User selectedUser = userService.selectUserById(Long.valueOf(cpf)); // Chama o método que seleciona o usuário por CPF

        // Verificação
        assertNotNull(selectedUser); // Verifica se o usuário retornado não é nulo
        assertEquals(testUser.getCpf(), selectedUser.getCpf()); // Verifica se os CPFs são iguais
        assertEquals(testUser.getName(), selectedUser.getName()); // Verifica se os nomes são iguais
        // Adicione mais verificações conforme necessário para outros atributos do usuário
    }

    @Test
    @DisplayName("Testa seleção de usuário por CPF quando o usuário não é encontrado")
    public void selectUserByCpfNotFound() {
        // Preparação
        String cpf = "99999999999"; // Define um CPF que não existe no sistema
        when(userRepository.findByCpf(cpf)).thenReturn(Optional.empty()); // Configura o mock para retornar um Optional vazio quando buscar pelo CPF

        // Execução e verificação
        CpfNotFoundException exception = assertThrows(CpfNotFoundException.class, () -> {
            userService.selectUserById(Long.valueOf(cpf));
        });

        // Verificação
        assertNotNull(exception); // Verifica se a exceção foi lançada
        assertEquals("O Cpf " + cpf + " não foi encontrado!", exception.getMessage()); // Verifica a mensagem da exceção
    }

    @Test
    @DisplayName("Testa exclusão de usuário por ID com sucesso")
    public void deleteUserByIdSuccess() {
        // Configura o mock para retornar a instância de usuário ao buscar pelo CPF
        when(userRepository.findById("12345678900")).thenReturn(Optional.of(testUser));

        // Chama o método a ser testado, passando o CPF do usuário a ser excluído
        userService.deleteUser("12345678900");

        // Verifica se o método deleteById foi invocado com o CPF correto
        verify(userRepository).deleteById("12345678900");
    }



    @Test
    @DisplayName("Testa exclusão de usuário por ID quando o usuário não é encontrado")
    public void deleteUserByIdNotFoundException() {
        // Preparação
        String cpf = "99999999999"; // Define um CPF que não existe no sistema
        when(userRepository.findByCpf(cpf)).thenReturn(Optional.empty()); // Configura o mock para retornar um Optional vazio quando buscar pelo CPF

        // Execução e Verificação
        assertThrows(CpfNotFoundException.class, () -> userService.deleteUser(cpf)); // Verifica se o método lança a exceção correta quando o usuário não é encontrado
    }


}