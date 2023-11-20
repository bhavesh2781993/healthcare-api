package com.test.api.healthcare.common.utils;

import static lombok.AccessLevel.PRIVATE;

import com.test.api.healthcare.common.exceptions.DataValidationException;

import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@NoArgsConstructor(access = PRIVATE)
public class FileCompressionUtil {

    private static final int NUMBER_OF_ATTEMPTS = 4;
    private static final int BUFFER_SIZE = 1024;

    public static byte[] compressFile(final MultipartFile file) {
        try {
            final var data = file.getBytes();
            final Deflater deflater = new Deflater();
            deflater.setLevel(Deflater.BEST_COMPRESSION);
            deflater.setInput(data);
            deflater.finish();

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            final byte[] tmp = new byte[NUMBER_OF_ATTEMPTS * BUFFER_SIZE];
            while (!deflater.finished()) {
                final int size = deflater.deflate(tmp);
                outputStream.write(tmp, 0, size);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch (final Exception e) {
            throw new DataValidationException("Failed to compress file");
        }
    }

    public static byte[] decompressData(final byte[] data) {
        final Inflater inflater = new Inflater();
        inflater.setInput(data);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        final byte[] tmp = new byte[NUMBER_OF_ATTEMPTS * BUFFER_SIZE];
        try {
            while (!inflater.finished()) {
                final int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (final Exception exception) {
            throw new DataValidationException("Failed to decompress data");
        }
        return outputStream.toByteArray();
    }
}
