package com.ezkorea.hybrid_app.web.controller.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wiper")
@RequiredArgsConstructor
public class WiperController {

    @GetMapping("/index")
    public String showWiperListPage() {
        return "wiper/main";
    }
}
