package com.tenco.blog._core.errors.exception;

// 400 Bad Request 상황에서 사용할 커스텀 예외 클래스
public class Exception404 extends RuntimeException{

    public Exception404(String message){
        super(message);
    }
    
    // 예시 - throw new Exception(" 에러 발생 ");
    // 필수 항목이 누락 되었을 경우 에러 발생
}
