package com.curator.common.configure;

import com.curator.common.properties.MinioProperties;
import com.curator.common.util.MinioUtil;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Minio 文件系统 自动配置
 *
 * @author Jun
 * @date 2021/4/18
 */
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties({MinioProperties.class})
public class MinioAutoConfiguration {

    private final MinioProperties properties;

    @Bean
    @SneakyThrows(Exception.class)
    @ConditionalOnProperty(name = "minio.url")
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder().endpoint(properties.getUrl())
                .credentials(properties.getAccessKey(),properties.getSecretKey()).build();
//        boolean flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
//        if(!flag) {
//            minioClient.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build());
//        }
        return minioClient;
    }

    @Bean
    @ConditionalOnBean(MinioClient.class)
    public MinioUtil minioUtil(MinioClient minioClient) {
        return new MinioUtil(minioClient);
    }

    public MinioAutoConfiguration(MinioProperties properties) {
        this.properties = properties;
    }
}
