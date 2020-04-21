package com.demo.uploads.feature.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "com.demo.uploads")
@Data
@Component
public class FileStorageProperties {

    private String directory;
}
