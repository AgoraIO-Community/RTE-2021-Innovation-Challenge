package com.kangaroo.openlive.utils.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
