package org.example.controller.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.DistrictDto;
import org.example.dto.UserCreateDto;
import org.example.entity.District;
import org.example.repository.DistrictRepository;
import org.example.service.DistrictService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserWebAuthController {

    private final UserService userService;
    private final DistrictService districtService;


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
}
