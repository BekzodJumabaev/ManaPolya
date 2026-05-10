package org.example.controller.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserCreateDto;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserWebAuthController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage(Model model){
        model.addAttribute("userDto", new UserCreateDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String sigup(@Valid @ModelAttribute("userDto") UserCreateDto userDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "signup";
        }
        userService.register(userDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
