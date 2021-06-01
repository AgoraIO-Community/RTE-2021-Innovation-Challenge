package io.agora.api.example.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.agora.api.example.R;

public class OnlineChatMirrorTool {
    public int mirrorStat = 1;

    public void onClickButton(int id) {
        if (id == R.id.btn_mirror)
            if (mirrorStat == 1) {
                mirrorStat = 2;
            } else {
                mirrorStat = 1;
            }
    }

    public void joinChannel(io.agora.rtc.RtcEngine engine) {
        engine.setLocalRenderMode(1, mirrorStat);
    }
}
