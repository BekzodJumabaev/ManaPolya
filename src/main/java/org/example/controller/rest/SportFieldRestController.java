package org.example.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.SportFieldCreateDto;
import org.example.dto.SportFieldResponceDto;
import org.example.service.SportFieldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/fields")
@RequiredArgsConstructor
public class SportFieldRestController {

    private final SportFieldService sportFieldService;

    @PostMapping("/create")
    public ResponseEntity<SportFieldResponceDto>  save(@Valid @RequestBody SportFieldCreateDto dto, Principal principal){
        SportFieldResponceDto responceDto = sportFieldService.create(principal.getName(), dto);
        return new ResponseEntity<>(responceDto,HttpStatus.CREATED);
    }
}
