package com.meva.finance.dtoTest;

import com.meva.finance.dto.FamilyDto;
import com.meva.finance.model.Family;
import com.meva.finance.repository.FamilyRepository;
import com.meva.finance.repository.UserRepository;
import com.meva.finance.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FamilyDtoTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FamilyRepository familyRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Testa conversão de FamilyDto para Family")
    public void testFamilyDtoConversion() {
        FamilyDto familyDto = new FamilyDto();
        familyDto.setIdFamily(1L);
        familyDto.setDescription("Test Family");

        Family family = familyDto.converterFamily();

        assertNotNull(family);
        assertEquals(familyDto.getIdFamily(), family.getIdFamily());
        assertEquals(familyDto.getDescription(), family.getDescription());
    }
}
