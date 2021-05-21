package com.bok.parent.helper;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class S3Helper {

    @Value("${minio.endpoint}")
    private transient String endpoint;

    @Value("${minio.accessKey}")
    private transient String accessKey;

    @Value("${minio.secretKey}")
    private transient String secretKey;

    private MinioClient minioClient;

    @PostConstruct
    public void initS3minio() {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }


    public Boolean upload(Long accountId, String folderPath, String fileName) {
        String accountIdString = String.valueOf(accountId);
        try {

            // create a bucket for the account if not exists.
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(accountIdString).build());
            if (!exists) {
                // Make a new bucket called as the accountId.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(accountIdString).build());
            }

            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(accountIdString)
                            .object(fileName)
                            .filename(folderPath + fileName)
                            .build());
            log.info("Successfully uploaded {} to bucket {}", fileName, accountId);
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Error occurred while uploading {} to bucket {} with cause: {}", fileName, accountId, e.getCause());
        }
        return true;
    }

    public void retrieve() {
    }
}

