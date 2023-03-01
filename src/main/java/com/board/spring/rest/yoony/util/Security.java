package com.board.spring.rest.yoony.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 보안 관련 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
public class Security {

  /**
   * 받은 String 데이터를 SHA-256으로 암호화하는 메소드
   * <p>String을 SHA-256으로 암호화하고, #bytesToHex()를 이용해 byte[]를 16진수로 변환하여 반환
   *
   * @param text 암호화할 String
   * @return String 암호화된 String
   * @throws NoSuchAlgorithmException
   * @author yoony
   * @version 1.0
   * @see #bytesToHex(byte[]) #bytesToHex(byte[])#bytesToHex(byte[])
   * @since 2023. 02. 26.
   */
  public static String sha256Encrypt(String text) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(text.getBytes());
    return bytesToHex(md.digest());
  }

  /**
   * byte[] 데이터를 16진수로 변환하는 메소드
   *
   * @param bytes 변환할 byte[]
   * @return String 변환된 Hex String
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  private static String bytesToHex(byte[] bytes) {
    StringBuilder builder = new StringBuilder();
    for (byte b : bytes) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }
}
