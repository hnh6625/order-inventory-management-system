package com.example.oims.catalog.application.service;

import com.example.oims.catalog.application.dto.AddVariantCommand;
import com.example.oims.catalog.application.dto.CreateStyleCommand;
import com.example.oims.catalog.domain.model.Style;
import com.example.oims.catalog.domain.repository.StyleRepository;
import com.example.oims.shared.exception.DuplicateStyleCodeException;
import com.example.oims.shared.exception.StyleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleApplicationService {

    private final StyleRepository styleRepository;

    public StyleApplicationService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    public void createStyle(CreateStyleCommand command) {
        if (styleRepository.existsByStyleCode(command.styleCode())) {
            throw new DuplicateStyleCodeException(command.styleCode());

        }

        Style style = new Style(
                command.styleCode(),
                command.name(),
                command.category()
        );

        styleRepository.save(style);
    }

    public void addVariant(AddVariantCommand command) {
        Style style = styleRepository.findByStyleCode(command.styleCode())
                .orElseThrow(() -> new StyleNotFoundException(command.styleCode()));

        style.addVariant(command.size(),command.color(),command.price());

        styleRepository.save(style);
    }

    public List<Style> getAllStyles() {
        return styleRepository.findAll();
    }
}
