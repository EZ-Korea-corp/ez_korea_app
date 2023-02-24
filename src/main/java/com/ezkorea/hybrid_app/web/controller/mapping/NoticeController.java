package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.service.notiece.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notice")
    public String showCreateNoticePage() {

        return "notice/create";
    }
}
