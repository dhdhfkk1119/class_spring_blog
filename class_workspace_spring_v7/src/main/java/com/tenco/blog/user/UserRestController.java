package com.tenco.blog.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog._core.errors.exception.Exception401;
import com.tenco.blog.utils.Define;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRestController {

    @Autowired
    private final UserService userService;

    // 회원 가입 API 설계
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserRequest.JoinDTO reqDTO) {
        log.info("회원가입 API 호출 - 사용자 명 : {} , 이메일 : {}" , reqDTO.getUsername(),reqDTO.getEmail());
        reqDTO.validate(); 
        UserResponse.JoinDTO joinDTO = userService.join(reqDTO);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new ApiUtil<>(joinDTO));
    }
    
    // 로그인 API 설계
    @PostMapping("/login")
    public ResponseEntity<ApiUtil<UserResponse.LoginDTO>> login(
        @RequestBody UserRequest.LoginDTO reqDto,
        HttpSession httpSession) {
        log.info("로그인 API 호출 - 사용자명 {} :" , reqDto.getUsername());
        reqDto.validate();

        UserResponse.LoginDTO loginDTO = userService.login(reqDto);
        httpSession.setAttribute(Define.SESSION_USER, loginDTO);

        return ResponseEntity.ok(new ApiUtil<>(loginDTO));
    }

    // 로그아웃 
    @PostMapping("/logout")
    public ResponseEntity<ApiUtil<String>> logout(HttpSession session) {
        log.info("로그아웃 API 호출 - {} : ");
        session.invalidate();
        return ResponseEntity.ok(new ApiUtil<>("로그아웃 성공"));
    }

    // 회원 유저 조회
    @GetMapping("/api/users/{id}")
    public ResponseEntity<ApiUtil<UserResponse.DetailDTO>> getUserInfo(
            @PathVariable(name = "id") Long id,
            HttpSession httpSession) {
        
        User session = (User) httpSession.getAttribute(Define.SESSION_USER);
        
        if(session != null) {
            throw new Exception401("로그인해주시기 바랍니다");
        }

        UserResponse.DetailDTO lDetailDTO = userService.findByUserId(id,session);
        return ResponseEntity.ok(new ApiUtil<>(lDetailDTO));
    }
    
    @PutMapping("/api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id")Long id,
                                        @RequestBody UserRequest.UpdateDTO updateDTO){
        // 인증 검사는 인터 셉터에서 처리 됨
        updateDTO.validate();

        UserResponse.UpdateDTO userResponse = userService.updateById(id,updateDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiUtil<>(userResponse));
    }
    
}
