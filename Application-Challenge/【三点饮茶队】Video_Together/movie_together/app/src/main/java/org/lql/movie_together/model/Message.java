package org.lql.movie_together.model;

public class Message {
    private String mSender;

    private String mContent;

    private int mType;

    public Message(int type, String sender, String content) {
        mType = type;
        mSender = sender;
        mContent = content;
    }

    public Message(String sender, String content) {
        this(0, sender, content);
    }

    public String getSender() {
        return mSender;
    }

    public String getContent() {
        return mContent;
    }

    public int getType() {
        return mType;
    }

    public static final int MSG_TYPE_TEXT = 1; // CHANNEL
}
