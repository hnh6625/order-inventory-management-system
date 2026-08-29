package com.example.oims.catalog.domain.model;

import com.example.oims.shared.Money;
import com.example.oims.shared.exception.DuplicateVariantException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;


class StyleTest {
    @Test
    void addVariant_shouldGenerateCorrectSku() {
        Style style = new Style("BT001", "Áo Basic Tee", "T-SHIRT");
        Color white = new Color("WHT", "White");
        Money price = new Money(new BigDecimal("150000"));

        Variant variant = style.addVariant(Size.M, white, price);

        assertEquals("BT001-WHT-M", variant.getSKU().getValue());
    }

    @Test
    void addVariant_shouldThrowWhenDuplicateSizeAndColor() {
        Style style = new Style("BT001", "Áo Basic Tee", "T-SHIRT");
        Color white = new Color("WHT", "White");
        Money price = new Money(new BigDecimal("150000"));
        style.addVariant(Size.M, white, price);

        assertThrows(DuplicateVariantException.class,
                () -> style.addVariant(Size.M, white, price));

    }

    @Test
    void addVariant_shouldAllowSameColorDifferentSize() {
        Style style = new Style("BT001", "Áo Basic Tee", "T-SHIRT");
        Color white = new Color("WHT", "White");
        Money price = new Money(new BigDecimal("150000"));

        style.addVariant(Size.M, white, price);
        style.addVariant(Size.L, white, price);

        assertEquals(2,style.getVariants().size());
    }

    @Test
    void getVariants_shouldReturnUnmodifiableList() {
        Style style = new Style("BT001","Áo Basic Tee", "T-SHIRT");

        assertThrows(UnsupportedOperationException.class,
                () -> style.getVariants().add(null));
    }

}
