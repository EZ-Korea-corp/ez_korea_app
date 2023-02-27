package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.service.notiece.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notice")
    public String showCreateNoticePage() {

        return "notice/create";
    }

    @GetMapping("/notice/{id}")
    public String showNoticeDetailPage(@PathVariable Long id, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(id));
        return "notice/detail";
    }

    @GetMapping("/notice/{id}/update")
    public String showNoticeUpdatePage(@PathVariable Long id, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(id));
        return "notice/update";
    }
}
