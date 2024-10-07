/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.photowey.minio.spring.boot.starter.example.template;

import io.github.photowey.minio.spring.boot.starter.example.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * {@code MinioTemplateTest}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/10/07
 */
@Slf4j
@SpringBootTest
class MinioTemplateTest extends AbstractTest {

    @Test
    void testCreateBuckets() {
        boolean ok = this.minioTemplate.createBucket("fromzero");
        Assertions.assertTrue(ok);

        boolean failed = this.minioTemplate.createBucket("fromzero");
        Assertions.assertFalse(failed);
    }

    @Test
    void testBucketExists() {
        boolean exists = this.minioTemplate.bucketExists("fromzero");
        Assertions.assertTrue(exists);
    }

    @Test
    void testUpload() throws Exception {
        Resource resource = new ClassPathResource("pictures/linkinpark_from_zero.jpg");
        String bucket = "fromzero";
        String object = "linkinpark_from_zero.jpg";
        this.minioTemplate.putObject(bucket, object, resource.getInputStream());
        String url = this.minioTemplate.url(bucket, object, 1, TimeUnit.MINUTES);
        log.info("the MinIO access url is: {}", url);
    }

    @Test
    void testDownload() throws Exception {
        String bucket = "fromzero";
        String object = "linkinpark_from_zero.jpg";
        try (InputStream stream = this.minioTemplate.downloadObject(bucket, object)) {
            String dir = System.getProperty("user.dir");
            if (!dir.endsWith(File.separator)) {
                dir += File.separator;
            }

            String tmpFile = dir + "linkinpark_from_zero.jpg";
            this.write(tmpFile, true, stream.readAllBytes());

            File file = new File(tmpFile);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
