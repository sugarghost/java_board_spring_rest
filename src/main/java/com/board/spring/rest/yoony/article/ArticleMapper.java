package com.board.spring.rest.yoony.article;

import com.board.spring.rest.yoony.article.search.SearchDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 게시글 관련 DAO 인터페이스
 * <p>mapper/ArticleMapper.xml에 정의된 쿼리를 실행하기 위한 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @see ArticleDTO
 * @since 2023. 02. 26.
 */
@Mapper
public interface ArticleMapper {

  /**
   * 게시글을 등록하는 메소드
   * <p>useGeneratedKeys="true"를 통해 insert 된 articleId가 매개변수 articleDTO에 자동으로 입력됨
   * <p>사용 전 articleDTO.isInsertArticleValid()를 통해 유효성 검사를 해야함
   *
   * @param articleDTO 게시글 정보를 담은 DTO articleId는 Auto Increment이므로 입력하지 않음
   *                   <p>createdDate는 current_timestamp로 입력되므로 입력하지 않음
   * @return int 등록된 게시글의 개수
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public int insertArticle(ArticleDTO articleDTO);

  /**
   * 게시글 개수를 조회하는 메소드
   *
   * @param searchDTO 검색 조건을 담은 DTO
   *                  <p>searchWord: 검색 키워드로 title, content, writer 세가지 컬럼에 like 연산처리
   *                  <p>categoryId: 카테고리 ID
   *                  <p>startDate: 날자 검색 시작일로 endDate와 함께 쓰이며 작성일 기간 검색에 사용
   *                  <p>endDate: 날자 검색 종료일로 startDate와 함께 쓰이며 작성일 기간 검색에 사용
   * @return int 조회된 게시글 개수
   * @author yoony
   * @version 1.0
   * @since 2023. 03. 04.
   */
  int selectArticleCount(SearchDTO searchDTO);

  /**
   * 게시글 목록을 조회하는 메소드
   *
   * @param searchDTO 검색 조건을 담은 DTO
   *                  <p>searchWord: 검색 키워드로 title, content, writer 세가지 컬럼에 like 연산처리
   *                  <p>categoryId: 카테고리 ID
   *                  <p>startDate: 날자 검색 시작일로 endDate와 함께 쓰이며 작성일 기간 검색에 사용
   *                  <p>endDate: 날자 검색 종료일로 startDate와 함께 쓰이며 작성일 기간 검색에 사용
   *                  <p>articlePerPage: 페이지당 보여줄 게시글 수
   *                  <p>pageNum: 페이지 번호
   * @return List ArticleDTO 리스트
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  List<ArticleDTO> selectArticleList(SearchDTO searchDTO);

  /**
   * 게시글을 조회해 DTO로 가져오는 메소드
   *
   * @param articleId 게시글 ID
   * @return {@link ArticleDTO} 조회된 게시글 정보를 담은 DTO
   * @aothor yoony
   * @version 1.0
   * @since 2023. 03. 04.
   */
  ArticleDTO selectArticle(long articleId);

  /**
   * 게시글의 존재여부를 조회하는 메소드
   *
   * @param articleId 게시글 ID
   * @return boolean 게시글이 존재하면 true, 존재하지 않으면 false
   * @aothor yoony
   * @version 1.0
   * @since 2023. 03. 04.
   */
  boolean isArticleExist(long articleId);

  /**
   * 특정 게시글에 대해서 입력한 패스워드가 일치하는지 확인
   *
   * @param articleDTO 검사 대상 articleId, password
   *                   <p>게시글 ID
   *                   <p>게시글 비밀번호 (SHA-256로 암호화된 값)
   * @return boolean 게시글 비밀번호가 입력된 비밀번호와 일치하면 true, 일치하지 않으면 false
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  boolean isPasswordCorrect(ArticleDTO articleDTO);

  /**
   * 게시글을 수정하는 메소드
   * <p>modeifiedDate는 current_timestamp로 입력되므로 입력하지 않음
   * <p>사용 전 {@link ArticleDTO#isUpdateArticleValid()}를 통해 유효성 검사를 해야함
   *
   * @param articleDTO 게시글 DTO
   *                   <p>writer, title, content가 update set 대상
   *                   <p>articleId, password를 기준으로 update
   *                   <p>password는 SHA-256으로 암호화된 값
   * @return int 수정된 게시글의 개수
   */
  int updateArticle(ArticleDTO articleDTO);

  /**
   * 게시글의 조회수를 1 증가시키는 메소드
   *
   * @param articleId 게시글 id
   * @return int update된 행의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  int updateArticleHit(long articleId);

  /**
   * 게시글을 삭제하는 메소드
   * <p>사용 전 {@link ArticleMapper#isPasswordCorrect(ArticleDTO)}를 통해 비밀번호가 일치하는지 확인을 권장
   * <p>쿼리에서도 비밀번호를 체크하지만, 보안을 위해 2차 검증
   *
   * @param articleDTO 삭제 대상 articleId, password
   *                   <p>게시글 ID
   *                   <p>게시글 비밀번호 (SHA-256로 암호화된 값)
   * @return int 삭제된 행의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 03. 04.
   */
  int deleteArticle(ArticleDTO articleDTO);
}
