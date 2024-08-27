package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.lang.annotation.Native;
import java.lang.annotation.Retention;
import java.lang.reflect.Method;

@RequiredArgsConstructor  //final 필드와 @NonNull이 붙은 필드에 대해 자동으로 생성자를 생성
@Component  //클래스를 Spring의 애플리케이션 컨텍스트에 빈으로 등록하기 위한 애너테이션
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter){  //MethodParameter parameter: 이 객체는 현재 처리 중인 메서드의 파라미터 정보
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;  //parameter.getParameterAnnotation(LoginUser.class): 현재 파라미터에 @LoginUser 어노테이션이 있는지 확인
                                                                                                    //!= null: 어노테이션이 있으면 true, 없으면 false를 반환
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;  //isLoginUserAnnotation && isUserClass: 파라미터가 @LoginUser 어노테이션이 붙어 있고, SessionUser 타입인 경우에만 true를 반환
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception{
        return httpSession.getAttribute("user");  //(SessionUser): httpSession.getAttribute("user")가 반환하는 객체를 SessionUser 타입으로 캐스팅(형 변환)


    }

}
