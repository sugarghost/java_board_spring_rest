package com.board.spring.rest.yoony.config;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Locale 설정을 위한 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

  /**
   * Locale 설정을 위한 메소드
   * <p>{@link CookieLocaleResolver}를 사용하여 Locale을 설정한다.
   * <p>기본 Locale은 한국어이며, 쿠키 이름은 "THYMELEAF_LANG"이다.
   *
   * @return LocaleResolver
   * @author yoony
   * @version 1.0
   * @see LocaleResolver
   * @see CookieLocaleResolver
   * @since 2023. 02. 26.
   */
  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(Locale.KOREAN);
    cookieLocaleResolver.setCookieName("THYMELEAF_LANG");
    return cookieLocaleResolver;
  }

  /**
   * Locale 변경을 위한 메소드
   * <p>{@link LocaleChangeInterceptor}를 사용하여 Locale을 변경한다.
   * <p>파라미터 이름은 "lang"이다.
   *
   * @return LocaleChangeInterceptor
   * @author yoony
   * @version 1.0
   * @see LocaleChangeInterceptor
   * @since 2023. 02. 26.
   */
  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
  }

  /**
   * Locale 변경을 위한 메소드
   * <p>{@link LocaleChangeInterceptor}를 사용하여 Locale을 변경한다.
   *
   * @param registry InterceptorRegistry
   * @author yoony
   * @version 1.0
   * @see LocaleChangeInterceptor
   * @since 2023. 02. 26.
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }

  /**
   * MessageSource 설정을 위한 메소드
   * <p>{@link ReloadableResourceBundleMessageSource}를 사용하여 MessageSource를 설정한다.
   * <p>기본 인코딩은 UTF-8이다.
   * <p>등록된 메시지는 "classpath:/message/messages", "classpath:/message/messages_error",
   * "classpath:/message/messages_thymeleaf"이다.
   *
   * @return MessageSource
   * @author yoony
   * @version 1.0
   * @see ReloadableResourceBundleMessageSource
   * @see ResourceBundleMessageSource
   * @see MessageSource
   * @since 2023. 02. 26.
   */
  @Bean
  public MessageSource messageSource() {

    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames("classpath:/message/messages",
        "classpath:/message/messages_error",
        "classpath:/message/messages_thymeleaf"
    );
    messageSource.setDefaultEncoding("UTF-8");

    return messageSource;
  }
}