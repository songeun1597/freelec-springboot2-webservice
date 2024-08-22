package com.jojoldu.book.springboot.domain.user;

import com.jojoldu.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter  //클래스의 모든 필드에 대해 getter 메서드를 자동으로 생성
@NoArgsConstructor  //매개변수가 없는 기본 생성자를 자동으로 생성
@Entity  //이 클래스가 JPA 엔티티임을 지정하여, 데이터베이스 테이블과 매핑
public class User extends BaseTimeEntity {

    @Id  // id 필드가 JPA 엔티티의 기본 키임을 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //@GeneratedValue는 기본 키 값을 자동으로 생성하는 전략을 설정,
                                                         // strategy 속성은 생성 전략을 정의, GenerationType.IDENTITY는 데이터베이스에서 기본 키 값을 자동으로 생성하도록 지시
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)  //enum 값이 데이터베이스에 문자열 형태로 저장되도록 설정
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){

        return this.role.getKey();
    }
}
