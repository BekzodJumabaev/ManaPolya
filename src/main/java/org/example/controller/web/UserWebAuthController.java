package org.example.controller.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.DistrictDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserResetPasswordDto;
import org.example.entity.District;
import org.example.entity.User;
import org.example.repository.DistrictRepository;
import org.example.repository.UserRepository;
import org.example.service.DistrictService;
import org.example.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserWebAuthController {

    private final UserService userService;
    private final DistrictService districtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("userDto", new UserCreateDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String sigup(@Valid @ModelAttribute("userDto") UserCreateDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.register(userDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/districts")
    @ResponseBody
    public List<DistrictDto> getDistrictsByRegion(@RequestParam Long regionId) {
        return districtService.getDistrictByRegion(regionId);
    }



    @GetMapping("/reset-password")
    public String showResetPasswordPage(Model model) {
        UserResetPasswordDto resetDto = new UserResetPasswordDto();
        resetDto.setPhoneNumber("");
        resetDto.setNewPassword("");

        model.addAttribute("resetDto", resetDto);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@ModelAttribute("resetDto") org.example.dto.UserResetPasswordDto dto,
                                       RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findByPhoneNumber(dto.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("Ushbu telefon raqamiga bog'langan foydalanuvchi topilmadi!"));

            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("message", "Parolingiz muvaffaqiyatli yangilandi! Yangi parol bilan tizimga kirishingiz mumkin.");
            return "redirect:/auth/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/reset-password";
        }
    }
}
