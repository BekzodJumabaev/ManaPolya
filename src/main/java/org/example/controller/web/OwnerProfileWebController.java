package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.dto.BookingResponceDto;
import org.example.dto.SportFieldResponceDto;
import org.example.service.BookingService;
import org.example.service.SportFieldService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerProfileWebController {


    private final SportFieldService sportFieldService;
    private final BookingService bookingService;

    @GetMapping("/dashboard")
    public String ownerDashboard(Model model, Principal principal){
        if (principal != null){
            return "redirect:/auth/login";
        }

        String username = principal.getName();

        List<SportFieldResponceDto> fieldsByOwner = sportFieldService.getFieldsByOwner(username);
        List<BookingResponceDto> bookinsByOwner = bookingService.getBookinsByOwner(username);

        model.addAttribute("myFields", fieldsByOwner);
        model.addAttribute("myBookings", bookinsByOwner);

        return "owner-dashboard";
    }

}
