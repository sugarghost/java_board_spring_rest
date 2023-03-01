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
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName CategoryController
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/v1/categories")
public class CategoryController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  CategoryService categoryService;

  @GetMapping
  public ResponseEntity getCategoryList() throws CustomException, Exception{
    return ResponseEntity.ok(categoryService.selectCategoryList());
  }
}
