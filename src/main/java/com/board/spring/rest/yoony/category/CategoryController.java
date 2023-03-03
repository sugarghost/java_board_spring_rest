package com.board.spring.rest.yoony.category;

import com.board.spring.rest.yoony.error.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * category 도메인을 처리하는 컨트롤러 /v1/categories 요청을 처리함
 *
 * @author YK
 * @version 1.0
 * @fileName CategoryController
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  CategoryService categoryService;

  /**
   * 카테고리 리스트를 반환하는 RequestMapping /v1/categories GET 요청을 처리함
   *
   * @return ResponseEntity 성공시 HttpStatus.OK와 카테고리 리스트 반환
   * @throws CustomException
   * @throws Exception
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  @GetMapping
  public ResponseEntity getCategoryList() throws CustomException, Exception {
    return ResponseEntity.ok(categoryService.selectCategoryList());
  }
}
