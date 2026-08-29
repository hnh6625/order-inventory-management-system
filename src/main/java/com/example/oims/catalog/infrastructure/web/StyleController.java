package com.example.oims.catalog.infrastructure.web;

import com.example.oims.catalog.application.dto.AddVariantCommand;
import com.example.oims.catalog.application.dto.CreateStyleCommand;
import com.example.oims.catalog.application.service.StyleApplicationService;
import com.example.oims.catalog.domain.model.Color;
import com.example.oims.catalog.domain.model.Style;
import com.example.oims.catalog.infrastructure.web.dto.AddVariantRequest;
import com.example.oims.catalog.infrastructure.web.dto.CreateStyleRequest;
import com.example.oims.catalog.infrastructure.web.dto.StyleResponse;
import com.example.oims.catalog.infrastructure.web.dto.VariantResponse;
import com.example.oims.shared.Money;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/styles")
public class StyleController {

    private final StyleApplicationService styleApplicationService;

    public StyleController(StyleApplicationService styleApplicationService) {
        this.styleApplicationService = styleApplicationService;
    }

    @PostMapping
    public ResponseEntity<Void> createStyle(@RequestBody @Valid CreateStyleRequest request) {
        CreateStyleCommand command = new CreateStyleCommand(
                request.styleCode(),
                request.name(),
                request.category()
        );

        styleApplicationService.createStyle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{styleCode}/variants")
    public ResponseEntity<Void> addVariant(@PathVariable String styleCode, @RequestBody @Valid AddVariantRequest request) {
        AddVariantCommand command = new AddVariantCommand(
                styleCode,
                request.size(),
                new Color(request.colorCode(),request.colorName()),
                new Money(request.price())
        );

        styleApplicationService.addVariant(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<StyleResponse>> getAllStyles() {
        List<Style> styles = styleApplicationService.getAllStyles();

        List<StyleResponse> response = styles.stream()
                .map(style -> new StyleResponse(
                        style.getId(),
                        style.getStyleCode(),
                        style.getName(),
                        style.getCategory(),
                        style.getVariants().stream()
                                .map(v -> new VariantResponse(
                                        v.getId(),
                                        v.getSKU().getValue(),
                                        v.getSize().name(),
                                        v.getColor().getCode(),
                                        v.getColor().getName(),
                                        v.getPrice().getAmount()
                                ))
                                .toList()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
