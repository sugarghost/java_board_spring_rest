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
 * @since 2023. 03. 04.
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
  private long categoryId;
  /**
   * 검색 시작 날짜
   */
  private String startDate;
  /**
   * 검색 종료 날짜
   */
  private String endDate;

  /**
   * 페이지당 보여줄 article 수
   */
  private int articlePerPage;

  /**
   * page number
   */
  private int pageNum;
}