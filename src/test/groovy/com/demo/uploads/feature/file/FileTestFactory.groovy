package com.demo.uploads.feature.file

import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class FileTestFactory {
    public static final FILE_CONTENT = "Some text".getBytes()
    public static final FILE_CONTENT_TYPE = MediaType.TEXT_PLAIN_VALUE
    public static final FILE_NAME = "file name with  spaces.txt"

    static MockMultipartFile newMultipartFile(Map map = [:]) {
        new MockMultipartFile("file", map.name ?: FileTestFactory.FILE_NAME, map.type ?: FileTestFactory.FILE_CONTENT_TYPE, map.content ?: FileTestFactory.FILE_CONTENT)
    }
}
