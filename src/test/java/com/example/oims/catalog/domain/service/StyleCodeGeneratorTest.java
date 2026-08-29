package com.example.oims.catalog.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StyleCodeGeneratorTest {
    private StyleCodeGenerator sgene;

    @BeforeEach
    public void setUp() {
        sgene = new StyleCodeGenerator();
    }

    @Test
    void generate_shouldReturnCorrectCode_whenValidInput() {
        String result = sgene.generate("Hoodie",2);

        assertEquals("HO002", result);
    }
    @Test
    void generate_shouldPadSequenceWith3Digits() {
        String result = sgene.generate("Hoodie",16);

        assertEquals("HO016", result);
    }
    @Test
    void generate_shouldHandleMaxSequence() {
        String result = sgene.generate("T-Shirt",166);

        assertEquals("TS166", result);
    }
    @Test
    void generate_shouldThrow_whenCategoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> sgene.generate(null,2));
    }
}
