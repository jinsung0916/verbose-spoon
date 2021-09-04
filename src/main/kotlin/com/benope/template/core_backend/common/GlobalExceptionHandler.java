package com.benope.template.core_backend.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public void handleUncaughtException(Exception ex, HttpServletRequest request) throws Exception {
        log.error(request.getRequestURI(), ex);
        throw ex; // Delegate to BasicErrorController.java
    }

}
