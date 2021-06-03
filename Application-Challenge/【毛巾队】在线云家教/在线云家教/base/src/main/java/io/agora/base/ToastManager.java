package io.agora.base;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public class ToastManager {
    private static Context sContext;
    private static Handler sHandler;

    public static void init(@NonNull Context context) {
        sContext = context.getApplicationContext();
        sHandler = new Handler();
    }

    public static void showShort(@StringRes int resId) {
        showShort(getContext().getString(resId));
    }

    public static void showShort(@StringRes int resId, Object... formatArgs) {
        showShort(getContext().getString(resId, formatArgs));
    }

    public static void showShort(@NonNull String text) {
        Context context = getContext();
        sHandler.post(() -> Toast.makeText(context, text, 300).show());
    }

    private static Context getContext() throws IllegalStateException {
        if (sContext == null)
            throw new IllegalStateException("ToastManager is not initialized. Please call init() before use!");
        return sContext;
    }
}
