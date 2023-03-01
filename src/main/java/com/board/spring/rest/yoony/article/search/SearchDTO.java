package com.board.spring.rest.yoony.article.search;

import com.board.spring.rest.yoony.util.ValidationChecker;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.apache.ibatis.type.Alias;


/**
 * 검색 조건을 관리하기 위한 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
@Data
@Alias("SearchDTO")
public class SearchDTO {

  /**
   * 검색어(제목, 내용, 작성자)
   */
  private String searchWord;
  /**
   * 카테고리 아이디
   */
  private String categoryId;
  /**
   * 검색 시작 날짜
   */
  private String startDate;
  /**
   * 검색 종료 날짜
   */
  private String endDate;

  /**
   * 시작 Row 번호
   */
  private int offset;

  /**
   * 가져올 Row 개수
   */
  private int limit;

}
