package com.demo.uploads.feature.file;

import com.demo.uploads.config.SecurityUtils;
import com.demo.uploads.feature.file.share.ShareE;
import com.demo.uploads.feature.file.share.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;
    private final ShareService shareService;

    @SneakyThrows
    @GetMapping(value = "/{fileId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public void getFile(@PathVariable String fileId, HttpServletResponse response) {
        FileE file = fileService.getOne(fileId);
        Long currentUserId = SecurityUtils.getCurrentUser().getId();
        if (!file.getOwner().getId().equals(currentUserId) && !shareService.isShared(fileId, currentUserId)) {
            throw new AccessDeniedException("Cannot get not own nor shared file");
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        IOUtils.copy(new FileInputStream(file.getPath()), response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping
    public MyFilesDto getMyFiles() {
        return MyFilesDto.builder()
                .owned(fileService.getOwnedFiles().stream().map(FileDto::map).collect(Collectors.toList()))
                .shared(shareService.findAllByUser(SecurityUtils.getCurrentUser())
                        .stream()
                        .map(ShareE::getFile)
                        .map(FileDto::map)
                        .collect(Collectors.toList()))
                .build();
    }

    @SneakyThrows
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String upload(@RequestParam MultipartFile file) {
        if (!MediaType.TEXT_PLAIN_VALUE.equals(file.getContentType())) {
            throw new HttpMediaTypeNotSupportedException("Only TEXT_PLAIN content type is allowed");
        }
        return fileService.create(file).getId();
    }
}
