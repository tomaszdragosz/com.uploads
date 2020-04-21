package com.demo.uploads.feature.file.share;

import com.demo.uploads.feature.user.UserE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareRepository extends JpaRepository<ShareE, Long> {
    List<ShareE> findAllByUser(UserE user);

    boolean existsByFileIdAndUserId(String fileId, Long userId);
}
