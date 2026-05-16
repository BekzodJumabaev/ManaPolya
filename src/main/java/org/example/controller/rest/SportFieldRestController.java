package org.example.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ImageResponceDto;
import org.example.dto.SportFieldCreateDto;
import org.example.dto.SportFieldResponceDto;
import org.example.dto.SportFileldSearchDto;
import org.example.service.ImageService;
import org.example.service.SportFieldService;
import org.example.utils.DataList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class SportFieldRestController {

    private final SportFieldService sportFieldService;
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<SportFieldResponceDto> save(@Valid @RequestBody SportFieldCreateDto dto, Principal principal){
        if (principal==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        SportFieldResponceDto responceDto = sportFieldService.create(principal.getName(), dto);
        return new ResponseEntity<>(responceDto,HttpStatus.CREATED);
    }

    @GetMapping
    public  ResponseEntity<DataList<List<SportFieldResponceDto>>> getAll(
            @RequestParam(required = false) SportFileldSearchDto searchDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        DataList<List<SportFieldResponceDto>> all = sportFieldService.getAll(searchDto, page, size);
        return ResponseEntity.ok(all);
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<ImageResponceDto> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        ImageResponceDto imageResponceDto = imageService.uploadImage(id, file);
        return new ResponseEntity<>(imageResponceDto,HttpStatus.CREATED);
    }
}
