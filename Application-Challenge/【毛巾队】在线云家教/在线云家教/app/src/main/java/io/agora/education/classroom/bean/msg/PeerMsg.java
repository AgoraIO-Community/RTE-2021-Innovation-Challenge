package io.agora.education.classroom.bean.msg;

import androidx.annotation.IntDef;

import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.agora.education.classroom.bean.JsonBean;

public class PeerMsg extends JsonBean {

    @Cmd
    public int cmd;
    public Object data;

    public PeerMsg(int cmd, Object data) {
        this.cmd = cmd;
        this.data = data;
    }

    @IntDef({Cmd.CO_VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Cmd {
        /**
         * co-video operation msg
         */
        int CO_VIDEO = 1;
    }

    public static class CoVideoMsg {
        @Type
        public int type;
        public String userId;
        public String userName;

        public CoVideoMsg(int type, String userId, String userName) {
            this.type = type;
            this.userId = userId;
            this.userName = userName;
        }

        @IntDef({Type.APPLY, Type.REJECT, Type.CANCEL, Type.ACCEPT, Type.ABORT, Type.EXIT})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Type {
            /**
             * student apply co-video
             */
            int APPLY = 1;
            /**
             * teacher reject apply
             */
            int REJECT = 2;
            /**
             * student cancel apply
             */
            int CANCEL = 3;
            /**
             * teacher accept apply
             */
            int ACCEPT = 4;
            /**
             * teacher abort co-video
             */
            int ABORT = 5;
            /**
             * student exit co-video
             */
            int EXIT = 6;
        }


        @IntDef({Status.DisCoVideo, Status.Applying, Status.CoVideoing})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Status {
            /**
             * 初始状态
             */
            int DisCoVideo = 0;
            /**
             * 申请中
             */
            int Applying = 1;
            /**
             * 连麦中
             */
            int CoVideoing = 2;
        }
    }

    public <T> T getMsg(Class<T> tClass) {
        return new Gson().fromJson(new Gson().toJson(data), tClass);
    }

    @Override
    public String toJsonString() {
        return super.toJsonString();
    }
}
