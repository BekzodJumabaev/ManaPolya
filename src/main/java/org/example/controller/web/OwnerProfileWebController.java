package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.dto.BookingResponceDto;
import org.example.dto.ProfileDashboardDto;
import org.example.dto.SportFieldResponceDto;
import org.example.service.BookingService;
import org.example.service.ProfileSrevice;
import org.example.service.SportFieldService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class OwnerProfileWebController {
    
    private final SportFieldService sportFieldService;
    private final BookingService bookingService;
    private final UserService userService;
    private final ProfileSrevice profileSrevice;

    @GetMapping("/dashboard")
    public String userDashboard(Model model, Principal principal){
        if (principal == null){
            return "redirect:/auth/login";
        }

        ProfileDashboardDto dashboardData = profileSrevice.getDashboardData(principal.getName());
        model.addAttribute("dashboard", dashboardData);

        return "owner-dashboard";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable("id") Long id,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id, principal.getName());
            redirectAttributes.addFlashAttribute("message", "Ijara muvaffaqiyatli bekor qilindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile/dashboard";
    }

}
