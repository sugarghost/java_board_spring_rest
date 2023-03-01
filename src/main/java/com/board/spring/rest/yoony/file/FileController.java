package com.board.spring.rest.yoony.file;

import com.board.spring.rest.yoony.error.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName CommentController
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/v1/articles/{articleId}/files")
public class FileController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @PostMapping
  public ResponseEntity createFile(@PathVariable long articleId)
      throws CustomException, Exception {
    return ResponseEntity.ok("Hello World");
  }

  @GetMapping
  public ResponseEntity getFileList(@PathVariable long articleId)
      throws CustomException, Exception {
    return ResponseEntity.ok("Hello World");
  }

  @GetMapping("/{fileId}")
  public ResponseEntity getFile(@PathVariable String fileId, @PathVariable long articleId)
      throws CustomException, Exception {
    return ResponseEntity.ok("Hello World");
  }

  @PutMapping("/{fileId}")
  public ResponseEntity updateFile(@PathVariable String fileId, @PathVariable long articleId)
      throws CustomException, Exception {
    return ResponseEntity.ok("Hello World");
  }

  @DeleteMapping
  public ResponseEntity deleteFileList(@PathVariable long articleId)
      throws CustomException, Exception {
    return ResponseEntity.ok("Hello World");
  }

  @DeleteMapping("/{fileId}")
  public ResponseEntity deleteFile(@PathVariable String fileId, @PathVariable long articleId)
      throws CustomException, Exception {
    return ResponseEntity.ok("Hello World");
  }
}
