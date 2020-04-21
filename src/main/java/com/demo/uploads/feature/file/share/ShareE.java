package com.demo.uploads.feature.file.share;

import com.demo.uploads.feature.file.FileE;
import com.demo.uploads.feature.user.UserE;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "share")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "file"})
public class ShareE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private UserE user;

    @NotNull
    @ManyToOne
    private FileE file;
}
