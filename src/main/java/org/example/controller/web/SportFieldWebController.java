package org.example.controller.web;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.dto.SportFieldCreateDto;
import org.example.dto.SportFieldResponceDto;
import org.example.dto.SportFieldUpdateDto;
import org.example.repository.RegionRepository;
import org.example.service.ImageService;
import org.example.service.SportFieldService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("isEdit", false);
        return "create-field";
    }

    @PostMapping("/create")
    public String saveField(@Valid @ModelAttribute("fieldDto") SportFieldCreateDto dto,
                            BindingResult bindingResult,
                            @RequestParam("images") List<MultipartFile> images,
                                                    Principal principal,
                                                    Model model){

        if (bindingResult.hasErrors()){
            model.addAttribute("regions", regionRepository.findAll());
            return "create-field";
        }

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

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id,  Model model,  Principal principal){
        SportFieldUpdateDto forUpdate = sportFieldService.getForUpdate(id);
        model.addAttribute("fieldDto", forUpdate);
        model.addAttribute("regions", regionRepository.findAll());
        model.addAttribute("isEdit", true);
        return "create-field";
    }

    @PostMapping("/{id}/edit")
    public String updateField(@PathVariable Long id,
                              @Valid @ModelAttribute("fieldDto") SportFieldUpdateDto dto,
                              BindingResult bindingResult,
                              Principal principal,
                              Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("regions", regionRepository.findAll());
            model.addAttribute("isEdit", true);
            return "create-field";
        }
        try {
            sportFieldService.update(id, dto, principal.getName());
            return "redirect:/profile/dashboard?message=Maydon muvaffaqiyatli yangilandi:";
        }catch (Exception e){
            model.addAttribute("error",e.getMessage());
            model.addAttribute("regions", regionRepository.findAll());
            model.addAttribute("isEdit", true);
            return "create-field";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteField(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            sportFieldService.delete(id, principal.getName());
            redirectAttributes.addFlashAttribute("message", "Maydon muvaffaqiyatli o'chirildi");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile/dashboard";
    }
}
