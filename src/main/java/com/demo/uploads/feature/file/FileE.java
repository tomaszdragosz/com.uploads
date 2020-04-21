package com.demo.uploads.feature.file;

import com.demo.uploads.feature.user.UserE;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "file")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileE {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
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

    @NotNull
    @OneToOne
    private UserE owner;
}
