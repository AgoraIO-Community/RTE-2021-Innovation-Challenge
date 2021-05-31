package com.qgmodel.qggame.model.exception;

/**
 * 与房间有关的异常封装
 * Created by HeYanLe on 2020/8/8 0008 16:59.
 * https://github.com/heyanLE
 */
public class RoomException extends Exception{

    public enum RoomExceptionEnum {

        /**
         * 房间不存在
         */
        ROOM_NOT_FOUND(0,"Room not found"),

        /**
         * 未登录
         */
        NOT_LOGGED(1,"Please login first ！"),

        /**
         * 房间已经满了
         */
        ROOM_FULL(2,"The room is full"),

        /**
         * 你已经在房间里
         */
        ALREADY_IN_ROOM(3,"You are already in the room！"),

        /**
         * 通用异常
         */
        GENERAL_ERROR(4,"It is failure");

        private int code;
        private String message;

        RoomExceptionEnum(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    private RoomExceptionEnum exceptionEnum;

    public int getCode() {
        return exceptionEnum.getCode();
    }

    public RoomException(String message, RoomExceptionEnum exceptionEnum) {
        super(message);
        this.exceptionEnum = exceptionEnum;
    }

    public static RoomException of(RoomExceptionEnum exceptionEnum) {
        return new RoomException(exceptionEnum.getMessage(), exceptionEnum);
    }
}
