package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.dto.BookingCreateDto;
import org.example.dto.BookingOfflineCreateDto;
import org.example.service.BookingService;
import org.example.service.SportFieldService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/fields")
@RequiredArgsConstructor
public class BookingWebController {

    private final BookingService bookingService;
    private final SportFieldService sportFieldService;

    @PostMapping("/{id}/book")
    public String bookField(
            @PathVariable("id") Long id,
            @ModelAttribute BookingCreateDto dto,
            Principal principal,
            RedirectAttributes redirectAttributes
            ){
       try {
           dto.setFieldId(id);
           bookingService.createBooking(dto, principal.getName());
           redirectAttributes.addFlashAttribute("message", "Maydon muvaffaqiyatli bron qilindi: ");
           return "redirect:/fields/" + id;
       }catch (Exception e){
           redirectAttributes.addFlashAttribute("error", e.getMessage());
           return "redirect:/fields/" + id;
       }
    }


    @PostMapping("/{id}/rate")
    public String rateField(@PathVariable("id") Long id,
                            @RequestParam("stars") Integer stars,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Maydonga baho berish uchun avval ro'yhatdan o'ting!");
            return "redirect:/fields/" + id;
        }

        try {
            sportFieldService.addRating(id, stars, principal.getName());
            redirectAttributes.addFlashAttribute("message", "Baho muvaffaqiyatli qabul qilindi. Rahmat!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/fields/" + id;
    }


    @PostMapping("/offline-book")
    public String processOfflineBooking(@ModelAttribute BookingOfflineCreateDto dto,
                                        Principal principal,
                                        RedirectAttributes redirectAttributes) {
        try {
            bookingService.createOfflineBooking(dto, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Offline bron muvaffaqiyatli saqlandi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile/dashboard";
    }
}
