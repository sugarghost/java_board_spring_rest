package com.board.spring.rest.yoony.file;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName FileService
 * @since 2023-02-28
 */
@Service
public class FileService {

  @Autowired
  private FileMapper fileMapper;

  public int insertFile(FileDTO fileDTO) {
    int result = fileMapper.insertFile(fileDTO);
    return result;
  }

  public List<FileDTO> selectFileList(long articleId) {
    List<FileDTO> fileList = fileMapper.selectFileList(articleId);
    return fileList;
  }

  public FileDTO selectFile(FileDTO fileDTO) {
    FileDTO file = fileMapper.selectFile(fileDTO);
    return fileDTO;
  }

  public boolean isArticleHasFile(long articleId) {
    boolean result = fileMapper.isArticleHasFile(articleId);
    return result;
  }

  public int deleteFile(FileDTO fileDTO) {
    int result = fileMapper.deleteFile(fileDTO);
    return result;
  }

  public int deleteAllFile(long articleId) {
    int result = fileMapper.deleteAllFile(articleId);
    return result;
  }

}
