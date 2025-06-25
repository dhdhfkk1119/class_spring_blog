package com.tenco.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final HttpSession session;
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
        System.out.println("=== 로그인 시도 ===");
        System.out.println("사용자명 : " + loginDTO.getUsername());

        try{
            loginDTO.validate();
            User user = userRepository.findByUsernameAndPassword(loginDTO.getUsername(),loginDTO.getPassword());
            if(user == null) {
                // 로그인 실패
                throw new IllegalArgumentException("사용자명 또는 비밀번호가 틀렸어");
            }

            // 로그인 성공
            session.setAttribute("sessionUser",user);
            // 로그인 성공 후 리스트 페이지 이동
            return "redirect:/";

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage","아이디 또는 비밀번호를 확인해주시기 바랍니다");
            // 필요하다면 에러메세지 생성에서 사용
            return "user/login-form";
        }


    }


    
    @GetMapping("/user/update-form")
    public String updateForm(HttpServletRequest httpServletRequest,HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("sessionUser");
        if(user == null){
            System.out.println("로그인 해주시기 바랍니다");
            return "redirect:/login-form";
        }
        httpServletRequest.setAttribute("user",user);
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO , HttpSession httpSession){
        User sessionUser = (User) httpSession.getAttribute("sessionUser");
        if(sessionUser == null){
            System.out.println("로그인 해주시기 바랍니다");
            return "redirect:/login-form";
        }
        updateDTO.validate();

        User user = userRepository.update(sessionUser.getId(),updateDTO);
        httpSession.setAttribute("sessionUser",user);
        // 세션 동기화
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO, HttpServletRequest httpServletRequest) {
        System.out.println("========== 회원 가입 요총 =============");
        System.out.println("사용자 이메일  :" + joinDTO.getEmail());
        try {
            // 1. 입력된 데이터 검증 (유효성 검사)
            joinDTO.validate();

            // 2. 사용자명 중복 체크
            User existUser = userRepository.findByUsername(joinDTO.getUsername());
            if (existUser != null) {
                throw new IllegalArgumentException("이미 존재하는 사용자명입니다 : " + existUser.getUsername());
            }
            // 3. DTO를 User Object 변환
            User user = joinDTO.toEntity();

            // 4. User Object 를 영속화 처리
            userRepository.save(user);
            return "redirect:/login-form";
        } catch (Exception e) {
            // 검증 실패시 보통 에러 메세지와 함께 다시 폼에 전달
            httpServletRequest.setAttribute("errorMessage", "잘못된요청이야");
            return "user/join-form";
        }

    }


}
