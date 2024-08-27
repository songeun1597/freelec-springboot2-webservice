package com.jojoldu.book.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)  //이 애너테이션이 적용될 수 있는 대상의 종류를 지정
                                //ElementType.PARAMETER: 이 애너테이션은 메서드 파라미터에 적용될 수 있음을 나타냄
@Retention(RetentionPolicy.RUNTIME)  //애너테이션 정보의 유지 기간을 지정
                                     //런타임 동안 유지되며, 리플렉션을 통해 접근할 수 있음
public @interface LoginUser {  //이 파일을 어노테이션 클래스로 지정
}
