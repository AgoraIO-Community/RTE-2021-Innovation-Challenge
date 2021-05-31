package org.lql.movie_together.model;

public class CurrentUserSettings {
    public int mEncryptionModeIndex;

    public String mEncryptionKey;

    public String mChannelName;

    public CurrentUserSettings() {
        reset();
    }

    public void reset() {
    }
}
