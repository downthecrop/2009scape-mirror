package org.runite.client;

import java.nio.ByteBuffer;

final class Class144_Sub1 extends Class144 {

    private ByteBuffer wrapped;

    final void setBytes(byte[] bytes) {
        this.wrapped = ByteBuffer.allocateDirect(bytes.length);
        this.wrapped.position(0);
        this.wrapped.put(bytes);
    }

    final byte[] getBytes() {
        byte[] bytes = new byte[this.wrapped.capacity()];
        this.wrapped.position(0);
        this.wrapped.get(bytes);
        return bytes;
    }

}
