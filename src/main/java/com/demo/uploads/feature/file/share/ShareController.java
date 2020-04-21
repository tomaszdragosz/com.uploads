package com.demo.uploads.feature.file.share;

import com.demo.uploads.config.SecurityUtils;
import com.demo.uploads.feature.file.FileE;
import com.demo.uploads.feature.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/share")
public class ShareController {

    private final FileService fileService;
    private final ShareService shareService;

    @PostMapping
    public void share(@RequestBody ShareDto share) {
        FileE file = fileService.getOne(share.getFileId());
        if (!file.getOwner().getId().equals(SecurityUtils.getCurrentUser().getId())) {
            throw new AccessDeniedException("Cannot share not own file");
        }
        shareService.shareFile(file, share.getEmail());
    }
}
