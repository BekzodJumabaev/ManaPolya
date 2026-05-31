package org.example.controller.rest;


import lombok.RequiredArgsConstructor;
import org.example.dto.SportFieldResponceDto;
import org.example.enums.FieldStatus;
import org.example.service.SportFieldService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final SportFieldService sportFieldService;

    // 1. Tasdiqlanmagan maydonlar ro'yxatini olish (Faqat ADMIN ko'ra oladi)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/fields/pending")
    public ResponseEntity<List<SportFieldResponceDto>> getPendingFields() {
        return ResponseEntity.ok(sportFieldService.getPendingFields());
    }

    // 2. Maydonni tasdiqlash yoki rad etish API'si
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/fields/{id}/moderate")
    public ResponseEntity<String> moderateField(@PathVariable Long id,
                                                @RequestParam FieldStatus status,
                                                @AuthenticationPrincipal UserDetails adminDetails) {

        sportFieldService.moderateField(id, status, adminDetails.getUsername());

        String message = status == FieldStatus.APPROVED
                ? "Maydon muvaffaqiyatli tasdiqlandi va ommaga e'lon qilindi!"
                : "Maydon rad etildi!";

        return ResponseEntity.ok("{\"message\": \"" + message + "\"}");
    }
}
