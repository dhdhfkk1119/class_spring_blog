package com.tenco.blog._core.errors;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog._core.errors.exception.*;
import jakarta.servlet.http.HttpServletRequest;

import java.net.http.HttpRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/* REST API 전용 예외 처리 핸들러
 * 모든 예외를 JSON 향태로 응답 처리
 */
@Order(1)
@RestControllerAdvice // 데이를 반환해서 내려 줄 때 사용
public class MyExceptionHandler {

    // slf4j 로거 생성 - 로깅 사용시 Sysout 대신 활용하는것이 좋다.
    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> ex400(Exception400 e, HttpServletRequest request) {
        log.warn("=== 400 Bad Request 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류: {}", e.getMessage());
        log.warn("User-Agent: {}", request.getHeader("User-Agent"));
        request.setAttribute("msg", e.getMessage());
        ApiUtil<?> apiUtil = new ApiUtil<>(400,e.getMessage());
        return new ResponseEntity<>(apiUtil,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> ex401ByData(Exception401 e, HttpServletRequest request) {
        log.warn("=== 401 Bad Request 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류: {}", e.getMessage());
        log.warn("User-Agent: {}", request.getHeader("User-Agent"));
        request.setAttribute("msg", e.getMessage());
        ApiUtil<?> apiUtil = new ApiUtil<>(400,e.getMessage());
        return new ResponseEntity<>(apiUtil,HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> ex403(Exception403 e, HttpServletRequest request) {
        log.warn("=== 403 Bad Request 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류: {}", e.getMessage());
        log.warn("User-Agent: {}", request.getHeader("User-Agent"));
        request.setAttribute("msg", e.getMessage());
        ApiUtil<?> apiUtil = new ApiUtil<>(400,e.getMessage());
        return new ResponseEntity<>(apiUtil,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<?> ex404(Exception404 e, HttpServletRequest request) {
        log.warn("=== 404 Bad Request 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류: {}", e.getMessage());
        log.warn("User-Agent: {}", request.getHeader("User-Agent"));
        request.setAttribute("msg", e.getMessage());
        ApiUtil<?> apiUtil = new ApiUtil<>(400,e.getMessage());
        return new ResponseEntity<>(apiUtil,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<?> ex500(Exception500 e, HttpServletRequest request) {
        log.warn("=== 500 Bad Request 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류: {}", e.getMessage());
        log.warn("User-Agent: {}", request.getHeader("User-Agent"));
        request.setAttribute("msg", e.getMessage());
        ApiUtil<?> apiUtil = new ApiUtil<>(400,e.getMessage());
        return new ResponseEntity<>(apiUtil,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 기타 모든 RuntimeException 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.warn("=== 예상치 못한 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("인증 오류: {}", e.getMessage());
        log.warn("User-Agent: {}", request.getHeader("User-Agent"));
        request.setAttribute("msg", e.getMessage());
        ApiUtil<?> apiUtil = new ApiUtil<>(400,e.getMessage());
        return new ResponseEntity<>(apiUtil,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
