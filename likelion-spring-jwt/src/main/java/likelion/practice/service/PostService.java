package likelion.practice.service;

import likelion.practice.entity.Post;
import likelion.practice.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final String uploadDir = "uploads/"; // 이미지 업로드 경로 설정

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시물 작성 메서드
    public Post createPost(String title, String content, List<MultipartFile> images) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        // 이미지 처리
        if (images != null) {
            for (MultipartFile image : images) {
                saveImage(image); // 이미지를 저장하는 메서드 호출
            }
        }

        return postRepository.save(post);
    }

    // 게시물 수정 메서드
    public Post updatePost(Long postId, String title, String content, List<MultipartFile> images) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(title);
        post.setContent(content);

        // 이미지 처리
        if (images != null) {
            for (MultipartFile image : images) {
                saveImage(image); // 이미지를 저장하는 메서드 호출
            }
        }

        return postRepository.save(post);
    }

    // 게시물 조회 메서드
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // 이미지 저장 메서드
    private void saveImage(MultipartFile image) {
        if (image.isEmpty()) {
            return; // 이미지가 비어있으면 아무 것도 하지 않음
        }
        try {
            // 파일을 저장할 경로
            Path path = Paths.get(uploadDir + image.getOriginalFilename());
            Files.createDirectories(path.getParent()); // 디렉토리 생성
            image.transferTo(path); // 파일 저장
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + image.getOriginalFilename(), e);
        }
    }
}
