package com.meva.finance.modelTest;

import com.meva.finance.model.Family;
import com.meva.finance.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setCpf("12345678900");
        user.setName("John Doe");
        user.setGenre("M");
        user.setBirth(LocalDate.of(1990, 5, 15));
        user.setState("California");
        user.setCity("Los Angeles");

        Family family = new Family();
        family.setIdFamily(1L);
        family.setDescription("Test Family");
        user.setFamily(family);
    }

    @Test
    @DisplayName("Testa construtor padrão")
    public void testDefaultConstructor() {
        User defaultUser = new User();
        assertNotNull(defaultUser);
    }

    @Test
    @DisplayName("Testa construtor com parâmetros")
    public void testParameterizedConstructor() {
        User parameterizedUser = new User("12345678900", "John Doe", "M",
                LocalDate.of(1990, 5, 15), "California", "Los Angeles", new Family());
        assertNotNull(parameterizedUser);
        assertEquals("12345678900", parameterizedUser.getCpf());
        assertEquals("John Doe", parameterizedUser.getName());
        assertEquals("M", parameterizedUser.getGenre());
        assertEquals(LocalDate.of(1990, 5, 15), parameterizedUser.getBirth());
        assertEquals("California", parameterizedUser.getState());
        assertEquals("Los Angeles", parameterizedUser.getCity());
        assertNotNull(parameterizedUser.getFamily());
    }
}
