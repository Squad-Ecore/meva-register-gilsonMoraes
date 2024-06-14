package com.meva.finance.modelTest;

import com.meva.finance.model.Family;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

public class FamilyTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testIdFamilyGetterSetter() {
        Long id = 1L;
        Family family = new Family();
        family.setIdFamily(id);
        assertEquals(id, family.getIdFamily());
    }

    @Test
    void testDescriptionGetterSetter() {
        String description = "Test Family";
        Family family = new Family();
        family.setDescription(description);
        assertEquals(description, family.getDescription());
    }

    @Test
    void testDescriptionValidation() {
        Family family = new Family();
        family.setDescription(null);

        var violations = validator.validate(family);
        assertFalse(violations.isEmpty(), "Descrição da família não pode ser nula");

        family.setDescription("");
        violations = validator.validate(family);
        assertFalse(violations.isEmpty(), "Descrição da família não pode ser vazia");

        family.setDescription("Test Family");
        violations = validator.validate(family);
        assertTrue(violations.isEmpty(), "Descrição da família válida");
    }

    @Test
    void testEqualsAndHashCode() {
        Family family1 = new Family();
        family1.setIdFamily(1L);
        Family family2 = new Family();
        family2.setIdFamily(1L);
        Family family3 = new Family();
        family3.setIdFamily(2L);

        assertTrue(family1.equals(family2) && family2.equals(family1));
        assertFalse(family1.equals(family3) || family3.equals(family1));
        assertEquals(family1.hashCode(), family2.hashCode());
        assertNotEquals(family1.hashCode(), family3.hashCode());
    }
}
