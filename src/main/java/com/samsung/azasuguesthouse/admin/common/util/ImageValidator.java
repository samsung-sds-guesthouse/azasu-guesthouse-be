package com.samsung.azasuguesthouse.admin.common.util;

import com.samsung.azasuguesthouse.common.exception.InvalidImageFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * 이미지 파일 유효성 검사 유틸리티.
 *
 * <p>검사 항목:
 * <ol>
 *   <li>파일 존재 여부</li>
 *   <li>파일 크기 (최대 {@value #MAX_FILE_SIZE_MB}MB)</li>
 *   <li>파일 확장자</li>
 *   <li>Content-Type (MIME 타입) — 클라이언트 제공값이므로 보조 검증 용도</li>
 *   <li>Magic Bytes (파일 시그니처) — 실제 파일 내용 기반 검증</li>
 * </ol>
 *
 * <p>확장자·Content-Type은 클라이언트가 자유롭게 조작할 수 있습니다.
 * Magic Bytes 검사를 추가해 실제 파일 내용이 허용된 이미지 형식인지 서버 측에서 직접 확인합니다.
 */
public final class ImageValidator {

    private static final int MAX_FILE_SIZE_MB = 5;
    private static final long MAX_FILE_SIZE_BYTES = (long) MAX_FILE_SIZE_MB * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );

    // Magic Bytes (파일 시그니처)
    // JPEG: FF D8 FF
    private static final byte[] MAGIC_JPEG = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    // PNG:  89 50 4E 47 0D 0A 1A 0A
    private static final byte[] MAGIC_PNG = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    // WebP: 52 49 46 46 .. .. .. .. 57 45 42 50  (RIFF....WEBP)
    private static final byte[] MAGIC_WEBP_RIFF = {0x52, 0x49, 0x46, 0x46};
    private static final byte[] MAGIC_WEBP_MARKER = {0x57, 0x45, 0x42, 0x50};
    private static final int MAGIC_BYTES_READ_LENGTH = 12;

    private ImageValidator() {}  // 유틸리티 클래스 - 인스턴스화 방지

    /**
     * 이미지 파일을 검증합니다.
     *
     * @param file 업로드된 파일
     * @throws InvalidImageFileException 유효하지 않은 파일인 경우
     */
    public static void validate(MultipartFile file) {
        checkNotEmpty(file);
        checkFileSize(file);
        checkExtension(file);
        checkContentType(file);
        checkMagicBytes(file);
    }

    private static void checkNotEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidImageFileException("이미지 파일이 없습니다.");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new InvalidImageFileException("파일명이 올바르지 않습니다.");
        }
    }

    private static void checkFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new InvalidImageFileException(
                    "파일 크기는 최대 " + MAX_FILE_SIZE_MB + "MB까지 허용됩니다."
            );
        }
    }

    private static void checkExtension(MultipartFile file) {
        String filename = file.getOriginalFilename(); // checkNotEmpty 이후이므로 null 아님
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            throw new InvalidImageFileException("확장자가 없는 파일은 업로드할 수 없습니다.");
        }
        String ext = filename.substring(dotIndex + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new InvalidImageFileException(
                    "지원하지 않는 확장자입니다. 허용 확장자: jpg, jpeg, png, webp"
            );
        }
    }

    private static void checkContentType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidImageFileException(
                    "지원하지 않는 파일 형식입니다. 이미지 파일(jpg, png, webp)만 업로드 가능합니다."
            );
        }
    }

    /**
     * 파일의 실제 바이트(Magic Bytes)를 읽어 이미지 시그니처를 검증합니다.
     *
     * <p>Content-Type과 파일 확장자는 클라이언트가 임의로 조작할 수 있으므로,
     * 서버가 파일 내용을 직접 검사하여 실제 이미지 형식 여부를 확인합니다.
     */
    private static void checkMagicBytes(MultipartFile file) {
        byte[] header = new byte[MAGIC_BYTES_READ_LENGTH];
        try (InputStream is = file.getInputStream()) {
            int bytesRead = is.readNBytes(header, 0, MAGIC_BYTES_READ_LENGTH);
            if (bytesRead < 3) {
                throw new InvalidImageFileException("파일 내용을 읽을 수 없습니다.");
            }
        } catch (IOException e) {
            throw new InvalidImageFileException("파일 내용을 읽는 중 오류가 발생했습니다.");
        }

        if (isJpeg(header) || isPng(header) || isWebp(header)) {
            return;
        }
        throw new InvalidImageFileException("실제 파일 내용이 이미지 형식(jpg, png, webp)이 아닙니다.");
    }

    private static boolean isJpeg(byte[] header) {
        return startsWith(header, MAGIC_JPEG);
    }

    private static boolean isPng(byte[] header) {
        return header.length >= MAGIC_PNG.length && startsWith(header, MAGIC_PNG);
    }

    private static boolean isWebp(byte[] header) {
        // RIFF....WEBP 구조 확인 (오프셋 0: RIFF, 오프셋 8: WEBP)
        if (header.length < 12) return false;
        if (!startsWith(header, MAGIC_WEBP_RIFF)) return false;
        for (int i = 0; i < MAGIC_WEBP_MARKER.length; i++) {
            if (header[8 + i] != MAGIC_WEBP_MARKER[i]) return false;
        }
        return true;
    }

    private static boolean startsWith(byte[] data, byte[] prefix) {
        if (data.length < prefix.length) return false;
        for (int i = 0; i < prefix.length; i++) {
            if (data[i] != prefix[i]) return false;
        }
        return true;
    }
}
