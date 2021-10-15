package com.carlos.ocean.config;

import com.carlos.util.file.MinioUtil;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Carlos Li
 * @date 2021/5/27
 */

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String entryPoint;
    private Integer port;
    private Boolean secure;
    private String rootUser;
    private String rootPass;
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(this.entryPoint, this.port, this.secure)
                .credentials(this.rootUser, this.rootPass)
                .build();
    }

    @Bean
    public MinioUtil minioUtil() {
        return new MinioUtil();
    }

}
