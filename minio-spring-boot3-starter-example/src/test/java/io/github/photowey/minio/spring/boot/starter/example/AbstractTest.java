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
package io.github.photowey.minio.spring.boot.starter.example;

import io.github.photowey.minio.spring.boot.autoconfigure.template.AsyncMinioTemplate;
import io.github.photowey.minio.spring.boot.autoconfigure.template.MinioTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * {@code AbstractTest}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/10/07
 */
public abstract class AbstractTest {

    @Autowired(required = false)
    protected MinioTemplate minioTemplate;
    @Autowired(required = false)
    protected AsyncMinioTemplate asyncMinioTemplate;

    protected void write(final String target, boolean quiet, final byte[] data) {
        try (RandomAccessFile raf = new RandomAccessFile(target, "rw");
             FileChannel channel = raf.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            buffer.flip();
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            channel.force(true);
        } catch (Exception e) {
            if (!quiet) {
                throw new RuntimeException(e);
            }
        }
    }
}

