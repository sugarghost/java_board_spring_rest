package com.board.spring.rest.yoony.file;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class FileProperty {
  @Value("${spring.servlet.multipart.location}")
  private String uploadPath;
}
