package com.board.spring.rest.yoony.file;

import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import com.board.spring.rest.yoony.util.FileUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * file 연관된 데이터 처리를 위한 서비스
 *
 * @author YK
 * @version 1.0
 * @fileName FileService
 * @since 2023-03-04
 */
@Service
public class FileService {

  @Autowired
  private FileMapper fileMapper;

  /**
   * 파일을 등록하는 메소드(비활성화)
   * <p> 파일 등록을 Article에서 트랜잭션을 통해 같이 진행하기 때문에 비활성화
   *
   * @param fileDTO 파일 정보를 담은 DTO
   * @return int 형태의 생성된 파일의 수
   * @throws CustomException (파일 생성 실패시 발생)
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public int insertFile(FileDTO fileDTO) {
    int result = fileMapper.insertFile(fileDTO);
    return result;
  }

  /**
   * 파일 목록을 가져오는 서비스
   *
   * @param articleId 파일 목록 가져올 게시글 번호
   * @return List<FileDTO> 형태의 파일 목록
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public List<FileDTO> selectFileList(long articleId) {
    List<FileDTO> fileList = fileMapper.selectFileList(articleId);
    return fileList;
  }

  /**
   * 파일 정보를 가져오는 서비스
   *
   * @param fileDTO 파일 정보를 담은 DTO
   * @return FileDTO 형태의 파일 정보
   * @throws CustomException (파일 정보가 없을 경우 발생)
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public FileDTO selectFile(FileDTO fileDTO) {
    FileDTO file = fileMapper.selectFile(fileDTO);
    if (file == null) {
      throw new CustomException(ErrorCode.FILE_NOT_FOUND);
    }
    return file;
  }

  /**
   * 파일이 존재하는지 확인하는 서비스(비활성화)
   * <p> 파일 존재 여부를 Article Query에서 확인하기 때문에 사용안함
   *
   * @param articleId 파일이 존재하는지 확인할 게시글 번호
   * @return boolean 형태의 파일 존재 여부
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public boolean isArticleHasFile(long articleId) {
    boolean result = fileMapper.isArticleHasFile(articleId);
    return result;
  }

  /**
   * 파일을 삭제하는 서비스(비활성화)
   * <p> 파일 삭제를 Article에서 트랜잭션을 통해 같이 진행하기 때문에 비활성화
   *
   * @param fileDTO 파일 정보를 담은 DTO
   * @return int 형태의 삭제된 파일의 수
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public int deleteFile(FileDTO fileDTO) {
    int result = fileMapper.deleteFile(fileDTO);
    return result;
  }

  /**
   * 게시글에 속한 모든 파일을 삭제하는 서비스(비활성화)
   * <p> 파일 삭제를 Article에서 같이 진행하기 때문에 비활성화
   *
   * @param articleId 파일을 삭제할 게시글 번호
   * @return int 형태의 삭제된 파일의 수
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public int deleteAllFile(long articleId) {
    int result = fileMapper.deleteAllFile(articleId);
    return result;
  }


  /**
   * 파일을 다운로드하는 서비스
   *
   * @param fileDTO     파일 정보를 담은 DTO
   * @param rangeHeader Range 헤더
   * @return ResponseEntity 형태의 파일 다운로드 정보
   * @throws Exception (파일 다운로드 실패시 발생)
   * @author YK
   * @version 1.0
   * @see FileUtil#downloadFile(FileDTO, List)
   * @since 2023-03-04
   */
  public ResponseEntity downloadFile(FileDTO fileDTO, List<String> rangeHeader) throws Exception {
    return FileUtil.downloadFile(fileDTO.getFilePath(),
        fileDTO.getFileSaveName(),
        fileDTO.getFileOriginName(),
        rangeHeader);
  }

}
