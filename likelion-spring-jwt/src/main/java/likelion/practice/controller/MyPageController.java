package likelion.practice.controller;

import likelion.practice.entity.User;
import likelion.practice.service.MyPageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    // 회원정보 조회 API
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestParam String userId) {
        User user = myPageService.getUserProfile(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // 프로필 수정 API
    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(@RequestParam String userId,
                                                  @RequestParam String name,
                                                  @RequestPart MultipartFile profileImage) { // @RequestPart 사용
        User updatedUser = myPageService.updateUserProfile(userId, name, profileImage);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
