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

    private final UserRepository userRepository;
    private final HttpSession session;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

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
        log.info("=== 로그인 시도 ===");
        log.info("사용자명 {} :" , loginDTO.getUsername());
        
        loginDTO.validate();
        User user = userRepository.findByUsernameAndPassword(loginDTO.getUsername(),loginDTO.getPassword());
        if(user == null) {
            // 로그인 실패
            throw new Exception401("사용자명 또는 비밀번호가 틀렸어");
        }

        // 로그인 성공
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
    public String updateForm(HttpServletRequest httpServletRequest,HttpSession httpSession) {
        
        log.info("회원 정보 수정 페이지 요청");

        User user = (User) httpSession.getAttribute("sessionUser");

        httpServletRequest.setAttribute("user",user);
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO , HttpSession httpSession){
        User sessionUser = (User) httpSession.getAttribute("sessionUser");

        updateDTO.validate();

        User user = userRepository.update(sessionUser.getId(),updateDTO);
        httpSession.setAttribute("sessionUser",user);
        // 세션 동기화
        return "redirect:/";
    }


    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO, HttpServletRequest httpServletRequest) {
        System.out.println("========== 회원 가입 요총 =============");
        System.out.println("사용자 이메일  :" + joinDTO.getEmail());
        // 1. 입력된 데이터 검증 (유효성 검사)
        joinDTO.validate();

        // 2. 사용자명 중복 체크
        User existUser = userRepository.findByUsername(joinDTO.getUsername());
        if (existUser != null) {
            throw new Exception404("존재 하지 않는 게시물입니다 ");
        }
        // 3. DTO를 User Object 변환
        User user = joinDTO.toEntity();

        // 4. User Object 를 영속화 처리
        userRepository.save(user);
        return "redirect:/login-form";

    }


}
