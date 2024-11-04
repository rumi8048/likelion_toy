package likelion.practice.service;

import likelion.practice.entity.User;
import likelion.practice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final String profileImageDirectory = "uploads/profile_images/"; // 프로필 이미지 업로드 경로 설정

    public MyPageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원정보 조회 기능
    public User getUserProfile(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));
    }

    // 프로필 수정 기능
    public User updateUserProfile(String userId, String name, MultipartFile profileImage) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

        user.setName(name);

        // 프로필 이미지 처리
        if (profileImage != null && !profileImage.isEmpty()) { // 이미지가 null이 아니고 비어있지 않은 경우
            String fileName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
            try {
                saveImage(profileImage, fileName); // 이미지 저장 메서드 호출
                user.setProfileImage(fileName); // 저장된 파일 이름을 사용자 프로필에 설정
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload profile image", e);
            }
        }

        return userRepository.save(user);
    }

    // 이미지 저장 메서드
    private void saveImage(MultipartFile image, String fileName) throws IOException {
        // 파일을 저장할 경로
        Path path = Paths.get(profileImageDirectory + fileName);
        Files.createDirectories(path.getParent()); // 디렉토리 생성
        image.transferTo(path); // 파일 저장
    }
}
