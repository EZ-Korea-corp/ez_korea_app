package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.service.etc.EtcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/etc")
@RequiredArgsConstructor
@Slf4j
public class EtcController {

    private final EtcService etcService;

    @GetMapping("/msgToCeo")
    public String showMsgToCeoPage() {
        return "etc/msgToCeo";
    }

    @GetMapping("/msgToCeo/{id}")
    public String showMsgToCeoDetailPage(Model model, @PathVariable Long id) {
        model.addAttribute("model", etcService.findMsgToCeoReg(id));
        return "etc/msgToCeoDetail";
    }

    @GetMapping("/msgToCeo/list")
    public String showMsgToCeoListPage(@RequestParam(value="page", defaultValue="0", required = false) int page,
                                       Model model) {
        model.addAttribute("msgList", etcService.msgToCeoList(page));

        return "etc/msgToCeoList";
    }
}
