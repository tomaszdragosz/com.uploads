package com.demo.uploads.feature.file;


import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {
    private String id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 1000)
    private String path;

    @NotNull
    @Size(max = 100)
    private String contentType;

    @NotNull
    private Long bytes;

    public static FileDto map(FileE fileE) {
        return FileDto.builder()
                .id(fileE.getId())
                .name(fileE.getName())
                .path(fileE.getPath())
                .contentType(fileE.getContentType())
                .bytes(fileE.getBytes())
                .build();
    }
}
