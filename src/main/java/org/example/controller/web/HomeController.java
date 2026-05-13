package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.dto.SportFieldResponceDto;
import org.example.service.SportFieldService;
import org.example.utils.DataList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SportFieldService sportFieldService;

    @GetMapping("/")
    public String homePage(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model){

        DataList<List<SportFieldResponceDto>> all = sportFieldService.getAll(search, page, size);
        model.addAttribute("fields", all.getData());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",  all.getTotalPages());
        model.addAttribute("search", search);
        return "index";
    }

    @GetMapping("/fields/{id}")
    public String fieldDetails(@PathVariable("id") long id, Model model){
        SportFieldResponceDto dto = sportFieldService.getById(id);
        model.addAttribute("field", dto);
        return "details";
    }
}
