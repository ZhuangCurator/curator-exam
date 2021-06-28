package com.curator.common.util;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

/**
 * 文件服务 工具类
 *
 * @author Jun
 * @date 2021/4/18
 */
@Configuration
public class MinioUtil {

    public static MinioClient minioClient;

    /**
     * 文件上传
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @param filePath:
     *            文件路径
     * @return: void
     */
    @SneakyThrows(Exception.class)
    public static String upload(String bucketName, String fileName, String filePath) {
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .filename(filePath)
                        .build());
        return getFileUrl(bucketName, fileName);
    }

    @SneakyThrows(Exception.class)
    public static String upload(String bucketName, String fileName, InputStream inputStream) {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(inputStream, inputStream.available(), -1).build();
        minioClient.putObject(args);
        return getFileUrl(bucketName, fileName);
    }

    private static String getFileUrl(String bucketName, String fileName) throws Exception{
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .method(Method.GET)
                        .build());
    }

    public MinioUtil(MinioClient minioClient) {
        MinioUtil.minioClient = minioClient;
    }
}
