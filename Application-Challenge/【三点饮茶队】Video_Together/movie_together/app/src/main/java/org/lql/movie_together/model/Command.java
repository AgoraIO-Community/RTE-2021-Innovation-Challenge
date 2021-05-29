package org.lql.movie_together.model;

import java.io.Serializable;

public class Command implements Serializable {
    public static final int COMMAND_MESSAGE = 0;
    public static final int COMMAND_START = 1;
    public static final int COMMAND_PAUSE = 2;
    public static final int COMMAND_SEEK_TO = 3;

    private String content;
    private int commandType;

    private Command(Builder builder) {
        this.content = builder.content;
        this.commandType = builder.commandType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    @Override
    public String toString() {
        return "Command{" +
                "content='" + content + '\'' +
                ", commandType=" + commandType +
                '}';
    }

    public static class Builder{
        private String content;
        private int commandType;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder commandType(int type) {
            this.commandType = type;
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }
}
