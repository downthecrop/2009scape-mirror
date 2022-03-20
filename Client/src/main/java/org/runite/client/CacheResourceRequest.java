package org.runite.client;

final class CacheResourceRequest extends ResourceRequest {

    Class41 cache;
    byte[] data;
    int type;

    final byte[] getData() {
        if (this.waiting) {
            throw new RuntimeException("Attempted to get data while request was not completed yet");
        }
        return this.data;
    }

    final int getCompletion() {
        return this.waiting ? 0 : 100;
    }

}
