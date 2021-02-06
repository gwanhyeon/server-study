package com.kgh.serverstudy.Exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    NAVER_API_UNAUTHORIZED_EXCEPTION("UNAUTHORIZED NAVER OPEN API ERROR!"),
    NAVER_API_ERROR_EXCEPTION("UNKNOWN NAVER OPEN API ERROR!"),
    INVALID_REQUEST_QUERY_EMPTY_EXCEPTION("INVALID QUERY ERROR!");
    private String message;
    ExceptionMessage(String message) {
        this.message = message;
    }
}
