package com.board.spring.rest.yoony.util;

import org.springframework.stereotype.Component;

/**
 * 유효성 검사를 위한 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
@Component
public class ValidationChecker {

  /**
   * String이 null이거나 빈 문자열인지 확인
   *
   * @param targetString 확인할 String
   * @return boolean null이거나 빈 문자열이면 true, 아니면 false
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public static boolean isEmpty(String targetString) {
    return targetString == null || "".equals(targetString) || targetString.isEmpty();
  }

  /**
   * Object가 null인지 확인
   * <p>CheckStringIsNullOrEmpty에 Object를 검사하려면 캐스팅 하는 과정이 필요했기 때문에 따로 만듬
   *
   * @param targetObject 확인할 Object
   * @return boolean null이면 true, 아니면 false
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public static boolean isEmpty(Object targetObject) {
    return targetObject == null || "".equals(targetObject);
  }


  /**
   * String의 길이가 지정한 StringLength보다 길면 StringLength만큼 자르고 "..."을 붙여서 반환
   *
   * @param targetString 자를 String
   * @param StringLength 자를 길이
   * @return substring된 targetString
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public static String SubStringWithSkipMark(String targetString, int StringLength) {
    if (targetString.length() > StringLength) {
      targetString = targetString.substring(0, StringLength) + "...";
    }
    return targetString;
  }
}
