package io.agora.education.service.bean.response;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.agora.education.classroom.bean.channel.User;

public class RecordRes {

    public int count;
    public int nextId;
    public int total;
    public List<RecordDetail> list;

    @IntDef({Status.RECORDING, Status.FINISHED, Status.DOWNLOADING, Status.CONVERTING, Status.UPLOADING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
        int RECORDING = 1;
        int FINISHED = 2;
        int DOWNLOADING = 3;
        int CONVERTING = 4;
        int UPLOADING = 5;
    }

    public static class RecordDetail {
        public String roomUuid;
        public String recordId;
        /**录制用户id*/
        public String recordUuid;
        public String boardId;
        public String boardToken;
        @Status
        public int status;
        public long startTime;
        public long endTime;
        public String url;

        public boolean isFinished() {
            return status == Status.FINISHED;
        }
    }

}
