package com.bok.parent.be.helper;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Component
public class S3Helper {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.port}")
    private Integer port;

    @Value("${minio.secure}")
    private Boolean secure;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    private MinioClient minioClient;

    @PostConstruct
    public void initS3minio() {
        minioClient = MinioClient.builder()
                .endpoint(endpoint, port, secure)
                .credentials(accessKey, secretKey)
                .build();
    }


    public Boolean upload(Long accountId, Path path) {
        String accountIdString = "account" + accountId;
        ObjectWriteResponse response;
        try {

            // create a bucket for the account if not exists.
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(accountIdString).build());
            if (!exists) {
                // Make a new bucket called as the accountId.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(accountIdString).build());
            }

            response = minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(accountIdString)
                            .object(path.getFileName().toString())
                            .filename(path.toString())
                            .build());
            log.info("Successfully uploaded {} to bucket {}", path, accountIdString);
            log.info("response: {}", response.etag());
            log.info("response: {}-{}-{}", response.region(), response.bucket(), response.object());
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Error occurred while uploading {} to bucket {} with cause: {}", path, accountId, e.getCause());
        } catch (EOFException eofe) {
            log.warn("EOF error in okhttp3.");
        } catch (IOException ioe) {
            log.error("IOE error {}", ioe.getCause());
        }
        return true;
    }

    public void retrieve() {
    }
}

