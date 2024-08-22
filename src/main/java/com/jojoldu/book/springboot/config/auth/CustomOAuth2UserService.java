package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.config.auth.dto.OAuthAttributes;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.user.User;
import com.jojoldu.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor  //final 필드와 @NonNull이 붙은 필드에 대해 자동으로 생성자를 생성
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {  //OAuth2UserRequest: OAuth2 인증 요청에 대한 정보를 담고 있는 객체
                                                                                                    //OAuth2User: 인증된 사용자 정보를 담고 있는 객체
                                                                                                    //제네릭 인터페이스는 인터페이스 선언에 제네릭 타입 파라미터를 포함할 수 있음
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

//        String accessToken = userRequest.getAccessToken().getTokenValue();  // 토큰 값 가져오기
//        System.err.println("Received OAuth2 Access Token: " + accessToken);  // 토큰 값 출력

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();  //delegate의 변수 타입: OAuth2UserService<OAuth2UserRequest, OAuth2User>
                                                                                                     //이 인터페이스는 OAuth2UserRequest를 입력으로 받아 OAuth2User를 반환하는 메서드를 제공하는 역할
        OAuth2User oAuth2User = delegate.loadUser(userRequest);  //delegate 변수(즉, DefaultOAuth2UserService 인스턴스)의 loadUser 메서드를 호출하여, userRequest라는 매개변수를 전달

        String registrationId = userRequest.getClientRegistration()
                                                .getRegistrationId();  //registrationId: 현재 진행중인 서비스를 구분하는 코드
        String userNameAttributeName = userRequest.getClientRegistration()
                                                        .getProviderDetails()
                                                            .getUserInfoEndpoint()
                                                                .getUserNameAttributeName();  //userNameAttributeName: Primary Key와 같은 의미
                                                                                              //구글의 경우 기본코드 지원("sub"), 네이버, 카카오 등은 지원 안함

        OAuthAttributes attributes = OAuthAttributes
                                        .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());  //of 메서드는 이 정보를 바탕으로 OAuthAttributes 객체를 생성

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),  //Collections.singleton(...): 단일 객체를 담고 있는 불변의 Set 컬렉션을 만듬
                                                                                       //예를 들어, 사용자의 역할이 "USER"라면, SimpleGrantedAuthority("USER")
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                                    .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))  //.map(entity -> ...): 만약 데이터베이스에서 사용자 정보를 찾았다면, 해당 사용자를 업데이트
                                        .orElse(attributes.toEntity());  //.orElse(attributes.toEntity()): 데이터베이스에 사용자가 없으면, OAuthAttributes 객체의 toEntity 메서드를 호출하여 새로운 User 객체를 생성

        return userRepository.save(user);
    }
}
