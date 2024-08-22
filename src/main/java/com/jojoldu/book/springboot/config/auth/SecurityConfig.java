package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor  //final 필드와 @NonNull이 붙은 필드에 대해 자동으로 생성자를 생성
@EnableWebSecurity  //Spring Security의 보안 기능을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {  //Spring Security에서 HTTP 보안 설정을 정의하는 메서드
        http
                .csrf().disable()  //CSRF(Cross-Site Request Forgery) 공격으로부터 보호하기 위한 설정을 비활성화
                                   // CSRF 보호를 비활성화하면, 클라이언트에서 CSRF 토큰을 보내지 않아도 됨
                .headers().frameOptions().disable()  //X-Frame-Options 헤더를 비활성화
                                                     //X-Frame-Options 헤더: 웹 페이지를 <iframe>, <frame>, 또는 <object> 요소에 포함시키는 것을 방지하여 클릭재킹 공격을 방지
                                                     //H2 데이터베이스 콘솔과 같은 도구를 <iframe>으로 로드할 수 있음
                .and()
                    .authorizeRequests()  //HTTP 요청에 대한 권한 부여 설정을 정의
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()  //특정 URL 패턴에 대한 요청을 인증 없이 접근할 수 있도록 허용
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())  ///api/v1/**로 시작하는 모든 경로에 대해 USER 역할을 가진 사용자만 접근할 수 있도록 설정
                    .anyRequest().authenticated()  //위의 패턴에 일치하지 않는 모든 요청에 대해 인증을 요구
                .and()
                    .logout()
                        .logoutSuccessUrl("/")  //로그아웃 성공 후 리디렉션할 URL을 설정
                .and()
                    .oauth2Login()  //OAuth2 로그인 기능을 활성화
                        .userInfoEndpoint()  //로그인 성공 이후 사용장 정보를 가져올때 설정 담당
                            .userService(customOAuth2UserService);  //사용자 정보를 처리할 인터페이스 구현체(customOAuth2UserService)를 등록
    }
}
