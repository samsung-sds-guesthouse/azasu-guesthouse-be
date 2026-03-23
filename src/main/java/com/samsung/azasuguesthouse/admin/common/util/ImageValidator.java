package com.samsung.azasuguesthouse.admin.common.util;

import com.samsung.azasuguesthouse.admin.common.exception.InvalidImageFileException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * 이미지 파일 유효성 검사 유틸리티.
 *
 * <p>검사 항목:
 * <ol>
 *   <li>파일 존재 여부</li>
 *   <li>파일 크기 (최대 {@value #MAX_FILE_SIZE_MB}MB)</li>
 *   <li>파일 확장자</li>
 *   <li>Content-Type (MIME 타입)</li>
 * </ol>
 *
 * <p>확장자만 검사하면 악의적인 사용자가 .jpg로 위장한 다른 파일을
 * 업로드할 수 있습니다. Content-Type을 함께 검사해 두 가지를 모두 방어합니다.
 */
public final class ImageValidator {

    private static final int MAX_FILE_SIZE_MB = 5;
    private static final long MAX_FILE_SIZE_BYTES = (long) MAX_FILE_SIZE_MB * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );

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
}
