package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.service.SportFieldService;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SportFieldWebController {

    private final SportFieldService sportFieldService;

}
