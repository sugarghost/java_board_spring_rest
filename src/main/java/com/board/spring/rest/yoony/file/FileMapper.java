package com.board.spring.rest.yoony.file;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 파일 관련 DAO 인터페이스
 * <p>mapper/FileMapper.xml에 정의된 쿼리를 실행하기 위한 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @see FileDTO
 * @since 2023. 02. 26.
 */
@Mapper
public interface FileMapper {

  /**
   * 파일을 등록하는 메소드
   *
   * @param fileDTO 파일 정보를 담은 DTO
   *                <p>fileId는 Auto Increment이므로 입력하지 않음
   *                <p>createdDate는 current_timestamp로 입력되므로 입력하지 않음
   * @return int 등록된 파일의 개수
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  int insertFile(FileDTO fileDTO);

  /**
   * 특정 게시글에 파일목록을 조회하는 메소드
   * <p> 사용자에게 저장된 파일의 이름이나 경로를 보여주지 않기 위해 fileSaveName, filePath는 제외됨
   *
   * @param articleId 파일을 조회할 대상 게시글 id
   * @return 조회된 파일 목록을 담은 {@link FileDTO} 리스트({@link List})
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  List<FileDTO> selectFileList(long articleId);

  /**
   * 특정 게시글의 특정 파일을 조회하는 메소드
   *
   * @param fileDTO 조회할 파일의 id와 게시글 id를 담은 {@link FileDTO}
   *                <p>key: articleId, value: 게시글 id
   *                <p>key: fileId, value: 파일 id
   * @return 조회된 파일 정보를 담은 {@link FileDTO}
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  FileDTO selectFile(FileDTO fileDTO);

  /**
   * 특정 게시글에 등록된 파일이 있는지 확인하는 메소드
   *
   * @param articleId 파일 개수를 조회할 대상 게시글 id
   * @return boolean 등록된 파일이 있으면 true, 없으면 false
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  boolean isArticleHasFile(long articleId);

  /**
   * 특정 게시글에 등록된 파일을 삭제하는 메소드
   *
   * @param fileDTO 삭제할 파일의 id와 게시글 id를 담은 {@link FileDTO}
   *                <p>key: articleId, value: 게시글 id
   *                <p>key: fileId, value: 파일 id
   * @return int 삭제된 파일의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  int deleteFile(FileDTO fileDTO);

  /**
   * 특정 게시글에 등록된 모든 파일을 삭제하는 메소드
   *
   * @param articleId 삭제할 파일들이 등록된 게시글 id
   * @return int 삭제된 파일의 개수
   * @aothor yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  int deleteAllFile(long articleId);
}
