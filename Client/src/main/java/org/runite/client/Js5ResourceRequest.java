package org.runite.client;

public final class Js5ResourceRequest extends ResourceRequest {

    protected byte padding;
    protected int anInt4067;
    protected DataBuffer data;

    public final int getCompletion() {
        return (this.data == null ? 0 : this.data.index * 100 / (-this.padding + this.data.buffer.length));
    }

    public final byte[] getData() {
        if (!this.waiting && this.data.buffer.length - this.padding <= this.data.index) {
            return this.data.buffer;
        } else {
            throw new RuntimeException();
        }
    }

}
