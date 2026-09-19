package com.islamnizami.taskboarddevlab.controller;


import com.islamnizami.taskboarddevlab.model.dto.LabelRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.LabelResponseDTO;
import com.islamnizami.taskboarddevlab.service.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @PostMapping
    public ResponseEntity<LabelResponseDTO> createLabel(
            @Valid @RequestBody LabelRequestDTO requestDTO,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(labelService.createLabel(requestDTO,authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<LabelResponseDTO>> getAllLabels(Authentication authentication){
        return ResponseEntity.ok(labelService.getAllLabels(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelResponseDTO> getLabelById(@PathVariable Long id,Authentication authentication){
        return ResponseEntity.ok(labelService.getLabelById(id,authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelResponseDTO> updateLabel(
            @PathVariable Long id,
            @Valid @RequestBody LabelRequestDTO requestDTO,
            Authentication authentication)
    {
        return ResponseEntity.ok(labelService.updateLabel(id, requestDTO, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(
            @PathVariable Long id,
            Authentication authentication )
    {
        labelService.deleteLabel(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }


}
