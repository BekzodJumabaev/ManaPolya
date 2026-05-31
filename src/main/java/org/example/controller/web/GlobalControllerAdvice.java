package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserService userService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Principal principal) {
        if (principal != null) {

            String fullname = userService.findByUsername(principal.getName());
            model.addAttribute("currentFullUsername", fullname);
        }
    }
}