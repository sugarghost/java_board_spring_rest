package com.board.spring.rest.yoony.category;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName CategoryService
 * @since 2023-02-28
 */
@Service
public class CategoryService {

  @Autowired
  private CategoryMapper categoryMapper;

  public List<CategoryDTO> selectCategoryList() {
    List<CategoryDTO> categoryList = categoryMapper.selectCategoryList();
    return categoryList;
  }

}
