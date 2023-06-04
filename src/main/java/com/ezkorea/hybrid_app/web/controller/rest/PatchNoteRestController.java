package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.app.util.ResponseData;
import com.ezkorea.hybrid_app.domain.note.PatchNote;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.notiece.PatchNoteService;
import com.ezkorea.hybrid_app.web.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PatchNoteRestController {

    private final PatchNoteService pnService;

    @PostMapping("/note")
    public ResponseData.ApiResult<?> createNewNote(@RequestBody NoticeDto dto,
                                                   @AuthenticationPrincipal SecurityUser securityUser) {

        if (!securityUser.getMember().getUsername().equals("dev")) {
            return ResponseData.error("글쓰기 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        PatchNote savedNote = pnService.saveNewNote(dto);

        return ResponseData.success(savedNote, "저장되었습니다.");
    }

    @PutMapping("/note/{id}")
    public ResponseEntity<Object> updateNote(@RequestBody NoticeDto dto, @PathVariable Long id,
                                                @AuthenticationPrincipal SecurityUser securityUser) {

        if (!securityUser.getMember().getUsername().equals("dev")) {
            return new ResponseEntity<>(Map.of("message", "글쓰기 권한이 없습니다."), HttpStatus.BAD_REQUEST);
        }

        PatchNote updatedNote = pnService.updateNote(id, dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", updatedNote.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/note/{id}")
    public ResponseEntity<Object> deleteNote(@RequestBody NoticeDto dto,
                                             @PathVariable Long id,
                                             @AuthenticationPrincipal SecurityUser securityUser) {


        if (!securityUser.getMember().getUsername().equals("dev")) {
            return new ResponseEntity<>(Map.of("message", "글쓰기 권한이 없습니다."), HttpStatus.BAD_REQUEST);
        }

        pnService.deleteNote(id);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

}
