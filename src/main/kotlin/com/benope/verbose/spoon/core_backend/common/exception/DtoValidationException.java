package com.benope.verbose.spoon.core_backend.common.exception;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DtoValidationException extends LocalizedException {

    private static final String MESSAGE_KEY = "message.empty";

    public DtoValidationException(List<FieldError> errors) {
        super(MESSAGE_KEY);
        super.setVararg(getErrorMessage(errors));
    }

    private String getErrorMessage(List<FieldError> errors) {
        return StringUtils.join(
                errors.stream()
                        .map(error -> error.getField() + " " + error.getDefaultMessage())
                        .collect(Collectors.toList()), ',');
    }

}
