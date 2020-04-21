package com.demo.uploads.feature.file.share

import lombok.AccessLevel
import lombok.NoArgsConstructor

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ShareTestFactory {

    static ShareDto newShareDto(Map map = [:]) {
        ShareDto.builder()
                .fileId(map.fileId ?: "123")
                .email(map.email ?: "mail@aa.aa")
                .build()
    }
}
