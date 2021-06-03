package io.agora.education.classroom.bean.board;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.herewhite.sdk.domain.GlobalState;

import java.util.List;

public class BoardState extends GlobalState implements Parcelable {
    private int follow = 0;
    private List<String> grantUsers;

    public BoardState() {
    }

    public BoardState(int follow, List<String> grantUsers) {
        this.follow = follow;
        this.grantUsers = grantUsers;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public List<String> getGrantUsers() {
        return grantUsers;
    }

    public void setGrantUsers(List<String> grantUsers) {
        this.grantUsers = grantUsers;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof BoardState)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        BoardState boardState = (BoardState) obj;
        return boardState.getFollow() == this.follow && boardState.getGrantUsers().equals(this.grantUsers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.follow);
        dest.writeStringList(this.grantUsers);
    }

    protected BoardState(Parcel in) {
        this.follow = in.readInt();
        this.grantUsers = in.createStringArrayList();
    }

    public static final Creator<BoardState> CREATOR = new Creator<BoardState>() {
        @Override
        public BoardState createFromParcel(Parcel source) {
            return new BoardState(source);
        }

        @Override
        public BoardState[] newArray(int size) {
            return new BoardState[size];
        }
    };
}
