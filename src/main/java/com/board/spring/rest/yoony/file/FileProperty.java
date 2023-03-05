package com.board.spring.rest.yoony.file;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class FileProperty {

  // TODO: Value는 너가 쓰고 싶을때 가져다 써라라는 의도이기 떄문에 따로 뺄 필요는 없음
  // 쓴다면 static final 으로 쓰는게 좋음
  @Value("${spring.servlet.multipart.location}")
  private String uploadPath;
}
