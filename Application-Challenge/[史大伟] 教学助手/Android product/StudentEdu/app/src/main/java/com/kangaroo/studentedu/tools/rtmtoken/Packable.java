package com.kangaroo.studentedu.tools.rtmtoken;

/**
 * Created by Li on 10/1/2016.
 */
public interface Packable {
    ByteBuf marshal(ByteBuf out);
}
