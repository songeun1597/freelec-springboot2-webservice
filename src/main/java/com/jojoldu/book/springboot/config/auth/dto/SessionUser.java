package com.jojoldu.book.springboot.config.auth.dto;

import com.jojoldu.book.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {  //Serializable: 이 인터페이스는 Java의 표준 인터페이스로, 이 인터페이스를 구현한 클래스의 객체는 "직렬화"될 수 있음
                                                    //직렬화: 객체를 네트워크를 통해 전송하거나 파일에 저장할 수 있도록 객체의 상태를 바이트 스트림으로 변환하는 과정
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
