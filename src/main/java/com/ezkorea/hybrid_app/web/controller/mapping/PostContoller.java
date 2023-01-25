package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.domain.post.Post;
import com.ezkorea.hybrid_app.service.post.PostService;
import com.ezkorea.hybrid_app.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostContoller {

    private final PostService postService;

    @GetMapping("/post/create")
    public String showPostPage() {

        return "post/post-create";
    }

    @PostMapping("/post/create")
    public String createPostPage(PostDto postDto, RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal SecurityUser securityUser) {
        log.info("dto={}", postDto.getTitle());
        Post post = postService.savePost(postDto, securityUser);

        redirectAttributes.addAttribute("id", post.getId());

        return "redirect:/post/detail/{id}";
    }

    @GetMapping("/post/detail/{id}")
    public String showPostDetailPage(@PathVariable Long id, Model model) {

        Post post = postService.findByPostId(id);

        model.addAttribute("post", post);

        return "post/post-detail";
    }
}
