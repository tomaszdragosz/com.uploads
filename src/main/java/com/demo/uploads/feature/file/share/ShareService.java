package com.demo.uploads.feature.file.share;

import com.demo.uploads.feature.file.FileE;
import com.demo.uploads.feature.user.UserE;
import com.demo.uploads.feature.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final UserService userService;
    private final ShareRepository repository;

    @Transactional
    public ShareE shareFile(FileE file, String email) {
        return repository.save(ShareE.builder()
                .file(file)
                .user(userService.findByEmail(email))
                .build());
    }

    public List<ShareE> findAllByUser(UserE user){
        return repository.findAllByUser(user);
    }

    public boolean isShared(String fileId, Long userId) {
        return repository.existsByFileIdAndUserId(fileId, userId);
    }
}
