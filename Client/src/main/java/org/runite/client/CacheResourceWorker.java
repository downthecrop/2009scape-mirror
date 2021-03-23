package org.runite.client;

import org.rs09.client.data.Queue;

import java.util.Objects;

final class CacheResourceWorker implements Runnable {

    private final Queue<CacheResourceRequest> requests = new Queue<>();
    private boolean stopped = false;
    private Thread thread;
    public int remaining = 0;

    private void addRequest(CacheResourceRequest request) {
        synchronized (this.requests) {
            this.requests.offer(request);
            this.remaining++;
            this.requests.notifyAll();
        }
    }

    public final void stop() {
        this.stopped = true;
        synchronized (this.requests) {
            this.requests.notifyAll();
        }

        try {
            this.thread.join();
        } catch (InterruptedException var4) {
        }

        this.thread = null;
    }

    public final void write(Class41 cache, int archive, byte[] data) {
        CacheResourceRequest var5 = new CacheResourceRequest();
        var5.data = data;
        var5.priority = false;
        var5.nodeKey = archive;
        var5.cache = cache;
        var5.type = 2;
        this.addRequest(var5);
    }

    public final CacheResourceRequest read(Class41 cache, int archive) {
        CacheResourceRequest var4 = new CacheResourceRequest();
        var4.cache = cache;
        var4.type = 3;
        var4.priority = false;

        var4.nodeKey = archive;
        this.addRequest(var4);
        return var4;
    }

    public final CacheResourceRequest priorityRead(Class41 cache, int archive) {
        CacheResourceRequest var4 = new CacheResourceRequest();
        var4.type = 1;
        synchronized (this.requests) {
            CacheResourceRequest var6 = this.requests.getFront();

            while (var6 != null) {
                if ((long) archive == var6.nodeKey && var6.cache == cache && var6.type == 2) {
                    var4.data = var6.data;
                    var4.waiting = false;
                    return var4;
                }

                var6 = this.requests.next();
            }
        }

        var4.data = cache.read(archive);
        var4.waiting = false;
        var4.priority = true;
        return var4;
    }

    public final void run() {
        while (!this.stopped) {
            CacheResourceRequest request;
            synchronized (this.requests) {
                request = this.requests.poll();
                if (request == null) {
                    try {
                        this.requests.wait();
                    } catch (InterruptedException var6) {
                    }
                    continue;
                }

                this.remaining--;
            }

            try {
                if (request.type == 2) {
                    request.cache.write((int) request.nodeKey, request.data.length, request.data);
                } else if (request.type == 3) {
                    request.data = request.cache.read((int) request.nodeKey);
                }
            } catch (Exception var5) {
                Class49.reportError(null, var5, (byte) 111);
            }

            request.waiting = false;
        }
    }

    public CacheResourceWorker() {
        Class64 var1 = Class38.signlink.method1451(5, this);

        while (Objects.requireNonNull(var1).anInt978 == 0) {
            TimeUtils.sleep(10L);
        }

        if (var1.anInt978 == 2) {
            throw new RuntimeException();
        } else {
            this.thread = (Thread) var1.anObject974;
        }
    }

}
