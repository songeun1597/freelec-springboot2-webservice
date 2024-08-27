package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.LoginUser;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.service.PostsService;
import com.jojoldu.book.springboot.web.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor  //final 필드와 @NonNull이 붙은 필드에 대해 자동으로 생성자를 생성
@Controller  //@Controller를 사용한 클래스는 스프링의 빈으로 등록되어 의존성 주입이 가능
public class IndexController {
    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {  //Model 객체는 뷰에 데이터를 전달하는 데 사용
                                                                     //@LoginUser SessionUser user는 사용자 세션에서 현재 로그인한 사용자의 정보를 주입받음
        model.addAttribute("posts", postsService.findAllDesc());  //postsService.findAllDesc()를 호출하여 게시글 목록을 가져오고, 이를 posts라는 이름으로 모델에 추가
                                                                     //이 데이터는 뷰에서 사용할 수 있음
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "chart";  //"chart"라는 이름의 뷰를 반환
    }

    @GetMapping("/desiner")
    public String reviewSave() {

        return "desiner";
    }



    @GetMapping("/posts/save")
    public String postsSave() {

        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){  //@PathVariable Long id: URL 경로에서 {id} 값을 추출하여 id 파라미터에 바인딩
        PostResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);  //조회한 게시글 정보를 모델에 post라는 이름으로 추가하여 뷰에서 사용할 수 있게 함

        return "posts-update";  //이 리졸버는 @LoginUser 애노테이션이 붙은 메서드 인자를 처리하기 위해 사용
    }

}
