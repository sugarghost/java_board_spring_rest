package com.board.spring.rest.yoony.util;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * 요청 처리를 위한 유틸리티 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
@Component
public class RequestUtil {

  /**
   * url에서 파라미터를 가져오는 메소드
   * <p>url의 ? 이후의 파라미터를 가져옴
   *
   * @param url 파라미터 가져올 대상 url
   * @return String 해당 url에 존재하는 파라미터
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public static String getUrlParameter(String url) {
    String[] urlSplit = url.split("\\?");
    if (urlSplit.length == 1) {
      return "";
    }
    return urlSplit[1];
  }


  /**
   * request에서 int형 파라미터를 가져올때 null인 경우 error가 발생해 util로 따로 빼서 만듬
   * <p>{@link ValidationChecker#isEmpty(String)} 검사 후 이상이 없으면 int형으로 변환해서 반환
   * <p>null인 파라미터에 대해서는 0을 반환함
   *
   * @param requestParameter 검사 대상 String
   * @return int 변화된 파라미터 값 혹은 0
   * @auther yoony
   * @version 1.0
   * @see ValidationChecker#isEmpty(String)
   * @since 2023. 02. 26.
   */
  public static int getIntParameter(String requestParameter) {
    return ValidationChecker.isEmpty(requestParameter) ? 0
        : Integer.parseInt(requestParameter);
  }


  /**
   * request에서 long형 파라미터를 가져올때 null인 경우 error가 발생해 util로 따로 빼서 만듬
   * <p>{@link ValidationChecker#isEmpty(String)} 검사 후 이상이 없으면 long형으로 변환해서 반환
   * <p>null인 파라미터에 대해서는 0을 반환함
   *
   * @param requestParameter 검사 대상 String
   * @return long 변화된 파라미터 값 혹은 0
   * @auther yoony
   * @version 1.0
   * @see ValidationChecker#isEmpty(String)
   * @since 2023. 02. 26.
   */
  public static long getLongParameter(String requestParameter) {
    return ValidationChecker.isEmpty(requestParameter) ? 0
        : Long.parseLong(requestParameter);
  }
}
