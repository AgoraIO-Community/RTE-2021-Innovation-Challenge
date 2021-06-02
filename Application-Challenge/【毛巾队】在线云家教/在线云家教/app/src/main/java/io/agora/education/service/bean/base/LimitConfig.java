package io.agora.education.service.bean.base;

public class LimitConfig {
    /**
     * 角色人数上限，-1不限
     */
    private int limit = -1;
    /**
     * 验证类型, 0: 允许匿名, 1: 不允许匿名 ，默认0
     */
    private int verifyType = VerifyType.Allow;
    /**
     * 0不订阅 1订阅，默认1
     */
    private int subscribe = SubscribeType.Subscribe;

    public LimitConfig(int limit) {
        this.limit = limit;
    }

    public LimitConfig(int limit, int verifyType, int subscribe) {
        this.limit = limit;
        this.verifyType = verifyType;
        this.subscribe = subscribe;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(int verifyType) {
        this.verifyType = verifyType;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }
}
