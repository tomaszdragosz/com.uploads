package com.demo.uploads.feature.file;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyFilesDto {
    private List<FileDto> owned;
    private List<FileDto> shared;
}
