package io.agora.education.classroom.bean.record;

import android.os.Parcel;
import android.os.Parcelable;

import io.agora.education.classroom.bean.JsonBean;

public class RecordBean extends JsonBean implements Parcelable {
    public static final String RECORD = "record";

    private String recordId;
    private int state;
    private long startTime;

    public RecordBean() {
    }

    public RecordBean(String recordId, int state, long startTime) {
        this.recordId = recordId;
        this.state = state;
        this.startTime = startTime;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toJsonString() {
        return super.toJsonString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recordId);
        dest.writeInt(this.state);
        dest.writeLong(this.startTime);
    }

    protected RecordBean(Parcel in) {
        this.recordId = in.readString();
        this.state = in.readInt();
        this.startTime = in.readLong();
    }

    public static final Creator<RecordBean> CREATOR = new Creator<RecordBean>() {
        @Override
        public RecordBean createFromParcel(Parcel source) {
            return new RecordBean(source);
        }

        @Override
        public RecordBean[] newArray(int size) {
            return new RecordBean[size];
        }
    };
}
