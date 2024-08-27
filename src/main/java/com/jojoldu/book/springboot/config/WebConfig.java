package com.jojoldu.book.springboot.config;

import com.jojoldu.book.springboot.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor  //final 필드와 @NonNull이 붙은 필드에 대해 자동으로 생성자를 생성
    @Configuration  //설정 클래스로 사용될 것을 나타내는 애노테이션
    public class WebConfig implements WebMvcConfigurer{
        private final LoginUserArgumentResolver loginUserArgumentResolver;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolver){  //HandlerMethodArgumentResolver의 목록을 의미
                                                                                                 // 이 목록에 새로운 커스텀 인자 리졸버를 추가하면, 스프링 MVC는 컨트롤러 메서드의 인자를 처리할 때 이 리졸버를 사용하게 됨
            argumentResolver.add(loginUserArgumentResolver);
    }

}
