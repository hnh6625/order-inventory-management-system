package com.example.oims.catalog.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ColorTest {
    @Test
    void constructor_shouldNormalizeCodeToUppercase() {
        Color color = new Color("wht", "White");
        assertEquals("WHT", color.getCode());
    }

    @Test
    void constructor_shouldThrowWhenCodeIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Color("", "White"));
    }

    @Test
    void constructor_shouldThrowWhenCodeIsNot3Characters() {
        assertThrows(IllegalArgumentException.class,
                () -> new Color("WH", "White"));   // 2 ký tự → lỗi

        assertThrows(IllegalArgumentException.class,
                () -> new Color("WHTE", "White")); // 4 ký tự → lỗi
    }

    @Test
    void equals_shouldCompareByCode() {
        Color c1 = new Color("WHT", "White");
        Color c2 = new Color("WHT", "White Shirt"); // name khác nhưng code giống
        assertEquals(c1, c2);
    }

}
