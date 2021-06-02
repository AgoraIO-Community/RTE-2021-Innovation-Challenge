package io.agora.base.network;

import androidx.annotation.Nullable;

public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    public BusinessException(int code, @Nullable String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(@Nullable String message) {
        this.code = -1;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }
}
