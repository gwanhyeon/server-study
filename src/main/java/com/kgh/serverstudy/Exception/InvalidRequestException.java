package com.kgh.serverstudy.Exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "INVALID REQUEST ERROR")
@NoArgsConstructor
public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
