package org.example.controller.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.dto.SportFieldCreateDto;
import org.example.dto.SportFieldResponceDto;
import org.example.repository.RegionRepository;
import org.example.service.ImageService;
import org.example.service.SportFieldService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fields")
public class SportFieldWebController {

    private final SportFieldService sportFieldService;
    private final RegionRepository regionRepository;
    private final ImageService imageService;


    @GetMapping("/create")
    public String createField(Model model){
        model.addAttribute("fieldDto", new SportFieldCreateDto());
        model.addAttribute("regions", regionRepository.findAll());
        return "create-field";
    }

    @PostMapping("/create")
    public String saveField(
                            @ModelAttribute("fieldDto") SportFieldCreateDto dto,
                            @RequestParam("images") List<MultipartFile> images,
                                                    Principal principal,
                                                    Model model){

        try {
            String currentUsername = principal.getName();
            SportFieldResponceDto responceDto = sportFieldService.create(currentUsername, dto);

            if (images != null && !images.isEmpty() && !images.get(0).isEmpty()) {
                imageService.uploadImages(responceDto.getId(),images);
            }
            return "redirect:/?message=Maydon muvaffaqiyatli qo'shildi:";
        }catch (Exception e){
            model.addAttribute("error",e.getMessage());
            model.addAttribute("regions", regionRepository.findAll());
            return "create-field";
        }

    }
}
