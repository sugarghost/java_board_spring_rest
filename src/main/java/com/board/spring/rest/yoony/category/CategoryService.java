package com.board.spring.rest.yoony.category;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 카테고리 관련 서비스
 *
 * @author YK
 * @version 1.0
 * @fileName CategoryService
 * @since 2023-03-04
 */
@Service
public class CategoryService {

  @Autowired
  private CategoryMapper categoryMapper;

  /**
   * 카테고리 리스트를 반환하는 메소드
   *
   * @return 카테고리 리스트
   * @throws Exception
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   * @see CategoryMapper#selectCategoryList()
   *
   */
  public List<CategoryDTO> selectCategoryList() {
    List<CategoryDTO> categoryList = categoryMapper.selectCategoryList();
    return categoryList;
  }

}
