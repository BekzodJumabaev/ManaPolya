package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.entity.SportField;
import org.example.enums.FieldStatus;
import org.example.enums.UserRole;
import org.example.repository.UserRepository;
import org.example.repository.SportFieldRepository;
import org.example.repository.BookingRepository;
import org.example.service.SportFieldService;
// Agar sizda UserService bo'lsa uni ham import qiling (pastdagi constructorda yozilgan)
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminWebController {

    private final SportFieldService sportFieldService;
    private final UserRepository userRepository;
    private final SportFieldRepository sportFieldRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService; // Buni to'g'irlab oling (ProfileSrevice bo'lsa o'shani yozing)

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("pendingFields", sportFieldService.getPendingFields());
        List<SportField> registeredFields = sportFieldRepository.findAll().stream()
                .filter(f -> f.getStatus() != FieldStatus.PENDING)
                .collect(Collectors.toList());
        model.addAttribute("approvedFields", registeredFields);
        model.addAttribute("allUsers", userRepository.findAllUsersForAdmin());
        model.addAttribute("transactions", bookingRepository.findAll());

        model.addAttribute("totalUsersCount", userRepository.count());
        model.addAttribute("totalFieldsCount", sportFieldRepository.countByStatus(FieldStatus.APPROVED));

        Double totalRevenue = bookingRepository.findAll().stream()
                .mapToDouble(b -> b.getTotalPrice().doubleValue()).sum();
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin-dashboard";
    }

    // 1. Arizalarni tasdiqlash yoki rad etish
    @PostMapping("/fields/{id}/moderate")
    public String moderateField(@PathVariable("id") Long id,
                                @RequestParam("status") FieldStatus status,
                                Principal principal, RedirectAttributes redirectAttributes) {
        try {
            sportFieldService.moderateField(id, status, principal.getName());
            redirectAttributes.addFlashAttribute("message", status == FieldStatus.APPROVED ? "Maydon tasdiqlandi!" : "Ariza rad etildi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }


    @PostMapping("/fields/{id}/delete")
    public String deleteFieldAsAdmin(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            sportFieldService.deleteAsAdmin(id);
            redirectAttributes.addFlashAttribute("message", "Sport maydoni tizimdan muvaffaqiyatli o'chirildi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }


    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "Foydalanuvchi bloklandi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }


    @PostMapping("/users/{id}/toggle-block")
    public String toggleUserBlock(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean isBlocked = userService.toggleBlock(id);
            redirectAttributes.addFlashAttribute("message", isBlocked ? "Foydalanuvchi tizimdan bloklandi!" : "Foydalanuvchi akkaunti faollashtirildi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/reset-password")
    public String resetUserPassword(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.resetPassword(id);
            redirectAttributes.addFlashAttribute("message", "Foydalanuvchi paroli '123456' ga muvaffaqiyatli o'zgartirildi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/change-role")
    public String changeUserRole(@PathVariable("id") Long id,
                                 @RequestParam("role") UserRole role,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserRole(id, role);
            redirectAttributes.addFlashAttribute("message", "Foydalanuvchi roli muvaffaqiyatli o'zgartirildi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}