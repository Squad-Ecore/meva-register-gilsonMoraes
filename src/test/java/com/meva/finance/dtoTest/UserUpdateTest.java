package com.meva.finance.dtoTest;

import com.meva.finance.dto.FamilyDto;
import com.meva.finance.dto.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserUpdateTest {

    private UserUpdate userUpdate;

    @BeforeEach
    public void setUp() {
        userUpdate = new UserUpdate();
        userUpdate.setName("John Doe");
        userUpdate.setGenre("M");
        userUpdate.setBirth(LocalDate.of(1990, 5, 15));
        userUpdate.setState("California");
        userUpdate.setCity("Los Angeles");

        FamilyDto familyDto = new FamilyDto();
        familyDto.setIdFamily(1L);
        familyDto.setDescription("Test Family");
        userUpdate.setFamilyDto(familyDto);
    }

    @Test
    @DisplayName("Testa criação com construtor padrão")
    public void testDefaultConstructor() {
        UserUpdate defaultUserUpdate = new UserUpdate();
        assertNotNull(defaultUserUpdate);
    }

    @Test
    @DisplayName("Testa criação com construtor com parâmetros")
    public void testParameterizedConstructor() {
        UserUpdate parameterizedUserUpdate = new UserUpdate();
        parameterizedUserUpdate.setName("Jane Doe");
        assertNotNull(parameterizedUserUpdate);
        assertEquals("Jane Doe", parameterizedUserUpdate.getName());
    }

    @Test
    @DisplayName("Testa acesso aos atributos")
    public void testAccessors() {
        assertEquals("John Doe", userUpdate.getName());
        assertEquals("M", userUpdate.getGenre());
        assertEquals(LocalDate.of(1990, 5, 15), userUpdate.getBirth());
        assertEquals("California", userUpdate.getState());
        assertEquals("Los Angeles", userUpdate.getCity());

        assertNotNull(userUpdate.getFamilyDto());
        assertEquals(1L, userUpdate.getFamilyDto().getIdFamily());
        assertEquals("Test Family", userUpdate.getFamilyDto().getDescription());
    }

    @Test
    @DisplayName("Testa igualdade com equals e hashCode")
    public void testEqualsAndHashCode() {
        UserUpdate anotherUserUpdate = new UserUpdate();
        anotherUserUpdate.setName("John Doe");
        anotherUserUpdate.setGenre("M");
        anotherUserUpdate.setBirth(LocalDate.of(1990, 5, 15));
        anotherUserUpdate.setState("California");
        anotherUserUpdate.setCity("Los Angeles");

        FamilyDto anotherFamilyDto = new FamilyDto();
        anotherFamilyDto.setIdFamily(1L);
        anotherFamilyDto.setDescription("Test Family");
        anotherUserUpdate.setFamilyDto(anotherFamilyDto);

        assertEquals(userUpdate, anotherUserUpdate);
        assertEquals(userUpdate.hashCode(), anotherUserUpdate.hashCode());
    }
}
