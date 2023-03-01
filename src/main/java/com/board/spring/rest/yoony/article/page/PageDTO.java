package com.board.spring.rest.yoony.article.page;

import lombok.Data;

/**
 * 페이징 처리를 위한 DTO 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
@Data
public class PageDTO {

  /**
   * 페이지 번호
   */
  private int pageNum;
  /**
   * 페이지당 보여줄 게시물 수
   */
  private int pageSize;
  /**
   * 페이지 블록당 보여줄 페이지 수(limit에서 활용)
   */
  private int blockPage;
  /**
   * 전체 게시물 수
   */
  private int totalCount;
  /**
   * 전체 페이지 수
   */
  private int totalPage;
  /**
   * 페이지 블록의 시작 페이지 번호
   */
  private int startPageNum;
  /**
   * 페이지 블록의 끝 페이지 번호
   */
  private int endPageNum;
  /**
   * 페이지 번호에 따른 시작 게시물 번호(offset에서 활용)
   */
  private int startRowNum;

  public void calculatePage(){
    if(pageSize == 0){
      return;
    }
    if(blockPage == 0){
      return;
    }
    if(totalCount == 0){
      return;
    }
    // 전체 페이지 수 = 전체 게시물 수 / 페이지당 보여줄 게시물 수
    this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    // 페이지 블록의 시작 페이지 번호 = (((현재 페이지 번호 - 1) / 페이지 블록당 보여줄 페이지 수) * 페이지 블록당 보여줄 페이지 수) + 1
    this.startPageNum = (((pageNum - 1) / blockPage) * blockPage) + 1;
    // 페이지 블록의 끝 페이지 번호 = 시작 페이지 번호 + 페이지 블록당 보여줄 페이지 수 - 1
    this.endPageNum = startPageNum + blockPage - 1;
    // 페이지 번호에 따른 시작 게시물 번호 = (현재 페이지 번호 - 1) * 페이지당 보여줄 게시물 수
    this.startRowNum = (pageNum - 1) * pageSize;
  }
}
