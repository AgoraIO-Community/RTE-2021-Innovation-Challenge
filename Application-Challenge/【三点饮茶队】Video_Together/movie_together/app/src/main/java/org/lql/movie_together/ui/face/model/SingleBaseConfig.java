package org.lql.movie_together.ui.face.model;

/**
 * author : shangrong
 * date : 2019/5/23 11:23 AM
 * description :配置BaseConfig单例
 */
public class SingleBaseConfig {
    private static AttributeBaseConfig baseConfig;

    private SingleBaseConfig() {

    }

    public static AttributeBaseConfig getBaseConfig() {
        if (baseConfig == null) {
            baseConfig = new AttributeBaseConfig();
        }
        return baseConfig;
    }

    public static void copyInstance(AttributeBaseConfig result) {
        baseConfig = result;
    }
}
