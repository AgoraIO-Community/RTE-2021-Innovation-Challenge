package com.agora.data.provider;

import androidx.annotation.NonNull;

public abstract class BaseMessageSource implements IMessageSource {
    protected IRoomProxy iRoomProxy;

    public BaseMessageSource(@NonNull IRoomProxy iRoomProxy) {
        this.iRoomProxy = iRoomProxy;
    }
}
