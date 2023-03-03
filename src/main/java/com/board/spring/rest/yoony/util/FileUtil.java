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

public class FileUtil {

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

  public static ResponseEntity downloadFile(FileDTO fileDTO, List<String> rangeHeader) throws Exception {
    Path filePath = Paths.get(fileDTO.getFilePath()).resolve(fileDTO.getFileSaveName()).normalize();
    Resource resource = new FileSystemResource(filePath.toFile());
    if (resource.exists() || resource.isReadable()) {
      long contentLength = resource.contentLength();
      if (rangeHeader != null && !rangeHeader.isEmpty()) {
        String range = rangeHeader.get(0);
        String[] rangeParts = range.split("=");
        String rangeUnit = rangeParts[0];
        String rangeValue = rangeParts[1];
        String[] rangeValues = rangeValue.split("-");
        long startRange = Long.parseLong(rangeValues[0]);
        long endRange = Math.min(Long.parseLong(rangeValues[1]), contentLength - 1);
        long downloadSize = Math.max(0, endRange - startRange + 1);
        if (rangeUnit.equals("bytes")) {
          return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
              .header(HttpHeaders.CONTENT_RANGE, "bytes " + startRange + "-" + endRange + "/" + contentLength)
              .header(HttpHeaders.ACCEPT_RANGES, "bytes")
              .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(downloadSize))
              .body(resource);
        }
      }
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileDTO.getFileOriginName() + "")
          .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
          .header(HttpHeaders.ACCEPT_RANGES, "bytes")
          .body(resource);
    }
    throw new CustomException(ErrorCode.FILE_NOT_FOUND);
  }
}
