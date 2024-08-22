package com.jojoldu.book.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {  //User: 이 제네릭 타입은 리포지토리가 관리할 엔티티 클래스
                                                                     //Long: 이 제네릭 타입은 User 엔티티의 ID 필드의 타입

    Optional<User> findByEmail(String email);  // Optional은 값이 없을 수도 있는 경우를 안전하게 처리
                                               // (이는 메서드가 User 객체를 반환할 수도 있고, 이메일이 일치하는 사용자가 없는 경우 Optional.empty()를 반환할 수도 있음)
}
