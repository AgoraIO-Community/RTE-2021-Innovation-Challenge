package io.agora.education.service.bean.base;

public class RoleConfig {
    private LimitConfig host;
    private LimitConfig assistant;
    private LimitConfig broadcaster;
    private LimitConfig audience;

    public RoleConfig() {
    }

    public RoleConfig(LimitConfig host, LimitConfig assistant, LimitConfig broadcaster,
                      LimitConfig audience) {
        this.host = host;
        this.assistant = assistant;
        this.broadcaster = broadcaster;
        this.audience = audience;
    }

    public LimitConfig getHost() {
        return host;
    }

    public void setHost(LimitConfig host) {
        this.host = host;
    }

    public LimitConfig getAssistant() {
        return assistant;
    }

    public void setAssistant(LimitConfig assistant) {
        this.assistant = assistant;
    }

    public LimitConfig getBroadcaster() {
        return broadcaster;
    }

    public void setBroadcaster(LimitConfig broadcaster) {
        this.broadcaster = broadcaster;
    }

    public LimitConfig getAudience() {
        return audience;
    }

    public void setAudience(LimitConfig audience) {
        this.audience = audience;
    }
}
