package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.dto.BookingResponceDto;
import org.example.dto.SportFieldResponceDto;
import org.example.dto.SportFileldSearchDto;
import org.example.repository.RegionRepository;
import org.example.service.BookingService;
import org.example.service.DistrictService;
import org.example.service.SportFieldService;
import org.example.service.UserService;
import org.example.utils.DataList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SportFieldService sportFieldService;
    private final RegionRepository regionRepository;
    private final DistrictService districtService;
    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping("/")
    public String homePage(
            @RequestParam(required = false)String search,
            @RequestParam(required = false)Long regionId,
            @RequestParam(required = false)Long districtId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model,
            Principal principal){

        if (principal != null){
            String fullname = userService.findByUsername(principal.getName());
            model.addAttribute("currentFullUsername", fullname);
        }
        SportFileldSearchDto searchDto = SportFileldSearchDto
                .builder()
                .search(search)
                .districtId(districtId)
                .regionId(regionId)
                .build();
        DataList<List<SportFieldResponceDto>> all = sportFieldService.getAll(searchDto, page, size);

        model.addAttribute("fields", all.getData());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",  all.getTotalPages());
        model.addAttribute("search", search);

        String currentRegionName = districtService.getCurrentRegionName(regionId, districtId);

        model.addAttribute("currentRegionName", currentRegionName);
        model.addAttribute("selectedRegionId", regionId);
        model.addAttribute("selectedDistrictId", districtId);
        model.addAttribute("regions", regionRepository.findAll());

        return "index";
    }

    @GetMapping("/fields/{id}")
    public String fieldDetails(@PathVariable("id") long id, Model model){
        SportFieldResponceDto dto = sportFieldService.getById(id);

        List<BookingResponceDto> occupiedSlots = bookingService.getActiveBookings(id);

        model.addAttribute("field", dto);
        model.addAttribute("occupiedSlots", occupiedSlots);
        return "details";
    }
}
