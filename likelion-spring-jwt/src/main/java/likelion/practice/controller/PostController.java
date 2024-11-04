package likelion.practice.controller;

import likelion.practice.entity.Post;
import likelion.practice.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시물 작성 API
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) List<MultipartFile> images) { // 변경: List<MultipartFile>
        Post post = postService.createPost(title, content, images);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    // 게시물 편집 API
    @PutMapping("/update/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) List<MultipartFile> images) { // 변경: List<MultipartFile>
        Post updatedPost = postService.updatePost(postId, title, content, images);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시물 조회 API
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        return ResponseEntity.ok(post);
    }
}
