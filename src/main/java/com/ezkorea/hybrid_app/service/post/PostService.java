package com.ezkorea.hybrid_app.service.post;

import com.ezkorea.hybrid_app.domain.member.SecurityUser;
import com.ezkorea.hybrid_app.domain.post.Post;
import com.ezkorea.hybrid_app.domain.post.PostRepository;
import com.ezkorea.hybrid_app.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post savePost(PostDto dto, SecurityUser securityUser) {
        Post newPost = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(securityUser.getMember())
                .build();

        return postRepository.save(newPost);
    }

    public Post findByPostId(Long id) {
        Optional<Post> post = postRepository.findById(id);

        return post.get();
    }

}