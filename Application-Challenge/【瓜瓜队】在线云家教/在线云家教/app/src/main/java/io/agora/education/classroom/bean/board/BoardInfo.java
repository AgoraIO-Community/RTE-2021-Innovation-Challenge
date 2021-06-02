package io.agora.education.classroom.bean.board;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class BoardInfo implements Parcelable {
    private String boardId;
    private String boardToken;

    public BoardInfo() {
    }

    public BoardInfo(String boardId, String boardToken) {
        this.boardId = boardId;
        this.boardToken = boardToken;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getBoardToken() {
        return boardToken;
    }

    public void setBoardToken(String boardToken) {
        this.boardToken = boardToken;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof BoardState)) {
            return false;
        }
        if(this == obj) {
            return true;
        }
        BoardInfo boardInfo = (BoardInfo) obj;
        return boardInfo.getBoardId().equals(this.boardId) && boardInfo.getBoardToken().equals(this.boardToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.boardId);
        dest.writeString(this.boardToken);
    }

    protected BoardInfo(Parcel in) {
        this.boardId = in.readString();
        this.boardToken = in.readString();
    }

    public static final Creator<BoardInfo> CREATOR = new Creator<BoardInfo>() {
        @Override
        public BoardInfo createFromParcel(Parcel source) {
            return new BoardInfo(source);
        }

        @Override
        public BoardInfo[] newArray(int size) {
            return new BoardInfo[size];
        }
    };
}
