package com.example.oims.catalog.application.service;

import com.example.oims.catalog.application.dto.AddVariantCommand;
import com.example.oims.catalog.application.dto.CreateStyleCommand;
import com.example.oims.catalog.domain.model.Color;
import com.example.oims.catalog.domain.model.Size;
import com.example.oims.catalog.domain.model.Style;
import com.example.oims.catalog.domain.repository.StyleRepository;
import com.example.oims.shared.Money;
import com.example.oims.shared.exception.DuplicateStyleCodeException;
import com.example.oims.shared.exception.StyleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StyleApplicationServiceTest {

    // mock repo, kh cần db thật
    private StyleRepository styleRepository;
    private StyleApplicationService service;

    @BeforeEach
    public void setUp() {
        styleRepository = Mockito.mock(StyleRepository.class);
        service = new StyleApplicationService(styleRepository);
    }

    @Test
    void createStyle_shouldSaveStyle_whenStyleCodeNotExists() {
        Mockito.when(styleRepository.existsByStyleCode("BT001"))
                .thenReturn(false);

        CreateStyleCommand command =
                new CreateStyleCommand("BT001", "Áo Basic Tee", "T-SHIRT");

        service.createStyle(command);

        Mockito.verify(styleRepository, Mockito.times(1))
                .save(Mockito.any(Style.class));
    }

    @Test
    void createStyle_shouldThrow_whenStyleCodeAlreadyExists() {
        Mockito.when(styleRepository.existsByStyleCode("BT001"))
                .thenReturn(true);

        CreateStyleCommand command =
                new CreateStyleCommand("BT001", "Áo Basic Tee", "T-SHIRT");

        assertThrows(DuplicateStyleCodeException.class, () -> service.createStyle(command));
    }

    @Test
    void addVariant_shouldThrow_whenStyleNotFound() {
        Mockito.when(styleRepository.findByStyleCode("BT001"))
                .thenReturn(Optional.empty());

        AddVariantCommand command =
                new AddVariantCommand(
                        "BT001",
                        Size.M,
                        new Color("WHT","WHITE"),
                        new Money(new BigDecimal("200000"))
                );

        assertThrows(StyleNotFoundException.class, () -> service.addVariant(command));
    }
}
