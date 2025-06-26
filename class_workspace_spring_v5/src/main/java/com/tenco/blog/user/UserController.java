package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception401;
import com.tenco.blog._core.errors.exception.Exception404;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {


    private final HttpSession session;
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    /*
     * 회원 가입 화면 요청
     * @return join-form.mustache
     * */

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    // 로그인 화면 요청
    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }


    // 로그인 액션 처리
    // 자원에 요청은 GET 방식이다 .단 로으인 요청은 예외 (보안상 이유)
    // DTO
    // 1. 입력 데이터 검증
    // 2. 사용자명과 비밀번호를 DB 접근해서 조회
    // 3. 로그인 성공 / 실패
    // 4. 로그인 성공이라면 서버측 메모리에 사용자 정보를 저장
    // 5. 메인 화면으로 리다이렉트 처리
    @PostMapping("login")
    public String login(UserRequest.LoginDTO loginDTO, Model model){

        loginDTO.validate();
        User user = userService.login(loginDTO);
        // 로그인 성공 세션 저장
        session.setAttribute("sessionUser",user);
        // 로그인 성공 후 리스트 페이지 이동
        return "redirect:/";

    }

    @GetMapping("/logout")
    public String logout() {
        log.info(" ==== 로그아웃 ==== ");
        session.invalidate();
        return "redirect:/";
    }

    
    @GetMapping("/user/update-form")
    public String updateForm(Model model,HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("sessionUser");
        userService.findById(user.getId());

        model.addAttribute("user",user);
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO , HttpSession httpSession){
        User sessionUser = (User) httpSession.getAttribute("sessionUser");

        updateDTO.validate();

        User user = userService.updateById(sessionUser.getId(),updateDTO);
        httpSession.setAttribute("sessionUser",user);

        // 세션 동기화 이게 무슨 말이냐? 로그인 하면 세션 값이 저장이 되는데 바로 동기화가 안되
        // 그렇기 때문에 회원정보를 바꾸자 마자 세션 정보가 바껴야함

        // 세션 동기화
        return "redirect:/";
    }


    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO, HttpServletRequest httpServletRequest) {
        joinDTO.validate();
        userService.join(joinDTO);
        return "redirect:/login-form";

    }


}
