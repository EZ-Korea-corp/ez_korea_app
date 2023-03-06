package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.note.PatchNote;
import com.ezkorea.hybrid_app.service.notiece.PatchNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_MASTER')")
public class PatchNoteController {

    private final PatchNoteService pnService;

    @GetMapping("/note")
    public String showPatchNotesPage(Model model, @RequestParam(value="page", defaultValue="0", required = false) int page) {
        Page<PatchNote> noteList = pnService.findAllNote(page);
        model.addAttribute("noteList", noteList);
        return "patch-note/list";
    }

    @GetMapping("/note/create")
    public String showCreatePatchNotesPage() {
        return "patch-note/create";
    }

    @GetMapping("/note/{id}")
    public String showDetailPatchNotesPage(@PathVariable Long id, Model model) {
        PatchNote currentNote = pnService.findById(id);
        model.addAttribute("notice", currentNote);
        return "patch-note/detail";
    }

    @GetMapping("/note/{id}/update")
    public String showUpdatePatchNotesPage(@PathVariable Long id, Model model) {
        PatchNote currentNote = pnService.findById(id);
        model.addAttribute("notice", currentNote);
        return "patch-note/update";
    }

}
