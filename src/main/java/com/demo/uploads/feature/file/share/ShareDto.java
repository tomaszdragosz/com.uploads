package com.demo.uploads.feature.file.share;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareDto {

    @NotNull
    private String fileId;

    @NotNull
    @Email
    private String email;
}
