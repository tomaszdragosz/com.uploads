package com.demo.uploads.feature.file;

import com.demo.uploads.feature.user.UserE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileE, String> {
    List<FileE> findAllByOwner(UserE owner);
}
