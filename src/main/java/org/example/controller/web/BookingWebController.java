package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.dto.BookingCreateDto;
import org.example.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/fields")
@RequiredArgsConstructor
public class BookingWebController {

    private final BookingService bookingService;

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
}
