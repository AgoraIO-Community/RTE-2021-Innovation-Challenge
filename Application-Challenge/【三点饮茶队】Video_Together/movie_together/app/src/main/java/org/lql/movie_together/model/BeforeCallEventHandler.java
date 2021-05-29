package org.lql.movie_together.model;

import io.agora.rtc.IRtcEngineEventHandler;

public interface BeforeCallEventHandler extends AGEventHandler {
    void onLastmileQuality(int quality);

    void onLastmileProbeResult(IRtcEngineEventHandler.LastmileProbeResult result);
}
