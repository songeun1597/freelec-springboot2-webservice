package com.jojoldu.book.springboot.config.auth.dto;

import com.jojoldu.book.springboot.domain.user.Role;
import com.jojoldu.book.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){  //static 키워드는 이 메서드를 클래스 레벨에서 호출할 수 있음
                                                                                                                            // 즉, 객체를 생성하지 않고도 OAuthAttributes.of(...) 형태로 호출할 수 있습
        if("naver".equals(registrationId)){  //"naver"는 문자열 리터럴이므로 null이 될 가능성이 없고, 이 방식은 registrationId가 null일 때도 NullPointerException을 피할 수 있음
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();  //builder()로 시작된 객체 생성 과정을 마무리하고, 최종적으로 OAuthAttributes 객체를 생성하여 반환
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");  //attributes.get("response"): attributes 맵에서 "response" 키에 해당하는 값을 가져옴. (네이버는 사용자 정보를 response라는 하위 맵에 포함시켜 제공)
                                                                                          //가져온 데이터를 Map<String, Object> 타입으로 캐스팅
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)  //엔티티를 생성하는 시점은 처음 가입할때이므로 기본권한은 GUEST로 부여
                .build();
    }




}
