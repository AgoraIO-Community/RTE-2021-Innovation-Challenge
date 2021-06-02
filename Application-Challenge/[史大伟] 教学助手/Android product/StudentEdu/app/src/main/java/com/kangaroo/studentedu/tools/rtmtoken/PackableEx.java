package com.kangaroo.studentedu.tools.rtmtoken;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
