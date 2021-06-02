package io.agora.base.callback;

import androidx.annotation.Nullable;

public interface ThrowableCallback<T> extends Callback<T> {
    void onFailure(@Nullable Throwable throwable);
}
