package com.meva.finance.dtoTest;

import com.meva.finance.dto.FamilyDto;
import com.meva.finance.dto.UserDto;
import com.meva.finance.model.User;
import com.meva.finance.repository.FamilyRepository;
import com.meva.finance.repository.UserRepository;
import com.meva.finance.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDtoTest {
    private FamilyDto testFamilyDto;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FamilyRepository familyRepository;

    @InjectMocks
    private UserService userService;
    @Test
    @DisplayName("Testa conversão de UserDto para User")
    public void testUserDtoConversion() {
        UserDto userDto = new UserDto();
        userDto.setCpf("12345678900");
        userDto.setName("Gilson");
        userDto.setGenre("M");
        userDto.setBirth(LocalDate.parse("2222-12-22"));
        userDto.setState("Rio de Janeiro");
        userDto.setCity("Nova Iguaçu");

        User user = userDto.converterUser();

        assertNotNull(user);
        assertEquals(userDto.getCpf(), user.getCpf());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getGenre(), user.getGenre());
        assertEquals(userDto.getBirth(), user.getBirth());
        assertEquals(userDto.getState(), user.getState());
        assertEquals(userDto.getCity(), user.getCity());
    }
}
