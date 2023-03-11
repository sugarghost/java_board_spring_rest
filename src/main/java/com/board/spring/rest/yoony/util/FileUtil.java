package com.board.spring.rest.yoony.util;

import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import com.board.spring.rest.yoony.file.FileDTO;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 및 다운로드를 위한 유틸 클래스
 *
 * @author YK
 * @version 1.0
 * @fileName FileUtil
 * @since 2023-03-04
 */
public class FileUtil {

  /**
   * 파일 업로드를 위한 메소드
   * <p> 파일 업로드를 진행하고, 업로드된 파일의 정보를 FileDTO 형태로 반환
   *
   * @param file       업로드할 파일
   * @param uploadPath 업로드할 경로
   * @return FileDTO 형태의 파일 정보
   * @throws Exception (파일 업로드 실패시 발생)
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public static FileDTO uploadFile(MultipartFile file, String uploadPath) throws Exception {

    if (!file.isEmpty()) {
      String originalFileName = file.getOriginalFilename();
      String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
      String newFileName = UUID.randomUUID() + "." + ext;
      file.transferTo(new File(newFileName));

      FileDTO fileDTO = new FileDTO();
      fileDTO.setFileOriginName(originalFileName);
      fileDTO.setFileSaveName(newFileName);
      fileDTO.setFileType(ext);
      fileDTO.setFilePath(uploadPath);
      return fileDTO;
    }
    return null;
  }

  /**
   * 파일 다운로드를 위한 메소드
   * <p> 파일 다운로드를 진행하고, 다운로드된 파일의 정보를 ResponseEntity 형태로 반환
   *
   * @param fileDTO     다운로드할 파일 정보를 담은 DTO
   * @param rangeHeader 다운로드 범위
   * @return ResponseEntity 형태의 파일 정보
   * @throws Exception (파일 다운로드 실패시 발생)
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public static ResponseEntity downloadFile(String fileSavePath, String fileSaveName, List<String> rangeHeader) throws Exception {
    Path filePath = Paths.get(fileSavePath).resolve(fileSaveName).normalize();
    Resource resource = new FileSystemResource(filePath.toFile());

    if (resource.exists() || resource.isReadable()) {
      long contentLength = resource.contentLength();
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;")
          .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
          .header(HttpHeaders.ACCEPT_RANGES, "bytes")
          .body(resource);
    }
    throw new CustomException(ErrorCode.FILE_NOT_FOUND);
  }
}
