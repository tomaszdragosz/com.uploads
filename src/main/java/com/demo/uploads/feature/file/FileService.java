package com.demo.uploads.feature.file;

import com.demo.uploads.config.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository repository;
    private final FileStorageProperties properties;

    @SneakyThrows
    @Transactional
    public FileE create(MultipartFile file) {
        FileE saved = repository.save(FileE.builder()
                .contentType(file.getContentType())
                .name(file.getOriginalFilename())
                .bytes(file.getSize())
                .owner(SecurityUtils.getCurrentUser())
                .build());
        String path = properties.getDirectory() + File.separator + saved.getId();
        FileUtils.writeByteArrayToFile(new File(path), file.getBytes());
        saved.setPath(path);
        return repository.save(saved);
    }

    @Transactional
    public FileE getOne(String id){
        return repository.getOne(id);
    }

    public List<FileE> getOwnedFiles() {
        return repository.findAllByOwner(SecurityUtils.getCurrentUser());
    }
}
