package com.kgh.serverstudy.Exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "OPEN API ERROR")
@NoArgsConstructor
public class OpenApiRuntimeException extends RuntimeException{
    public OpenApiRuntimeException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
