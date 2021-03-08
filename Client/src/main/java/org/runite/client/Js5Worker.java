package org.runite.client;

import org.rs09.client.data.Queue;
import org.rs09.client.net.Connection;

import java.io.IOException;

final class Js5Worker {

    private final Queue<Js5ResourceRequest> priorityRequests = new Queue<>();
    private final Queue<Js5ResourceRequest> pendingPriorityRequests = new Queue<>();
    private final Queue<Js5ResourceRequest> normalRequests = new Queue<>();
    private final Queue<Js5ResourceRequest> pendingNormalRequests = new Queue<>();

    private long lastAttempt;
    private Connection connection;
    private int latency;
    private byte aByte1009 = 0;
    private Js5ResourceRequest current;

    private final DataBuffer outgoing = new DataBuffer(4);
    private final DataBuffer incoming = new DataBuffer(8);

    public volatile int status = 0;
    public volatile int errors = 0;

    public final boolean normalRequestsFull() {
        return 20 <= this.countNormalRequests();
    }

    public final boolean process() {
        int available;

        if (this.connection != null) {
            long time = TimeUtils.time();
            int delta = (int) (time - this.lastAttempt);
            this.lastAttempt = time;
            if (delta > 200) {
                delta = 200;
            }

            this.latency += delta;
            if (this.latency > 30000) {
                try {
                    this.connection.close();
                } catch (Exception var18) {
                }

                this.connection = null;
            }
        }

        if (this.connection == null) {
            return this.countPriorityRequests() == 0 && this.countNormalRequests() == 0;
        }

        try {
            this.connection.checkErrors();

            // Send any requests
            Js5ResourceRequest request;
            for (request = this.priorityRequests.getFront(); request != null; request = this.priorityRequests.next()) {
                this.outgoing.index = 0;
                this.outgoing.writeByte(1); //High priority JS5 request
                this.outgoing.writeMedium((int) request.nodeKey);

                this.connection.sendBytes(this.outgoing.buffer, 4);
                this.pendingPriorityRequests.offer(request);
            }

            for (request = this.normalRequests.getFront(); request != null; request = this.normalRequests.next()) {
                this.outgoing.index = 0;
                this.outgoing.writeByte(0); //Low priority JS5 request
                this.outgoing.writeMedium((int) request.nodeKey);
                this.connection.sendBytes(this.outgoing.buffer, 4);
                this.pendingNormalRequests.offer(request);
            }

            // Handle incoming requests (max. 100 per iter)
            for (int i = 0; 100 > i; ++i) {
                available = this.connection.availableBytes();
                if (available < 0) {
                    throw new IOException();
                }

                if (available == 0) {
                    break;
                }

                this.latency = 0;
                byte headerSize = 0;
                if (this.current == null) {
                    headerSize = 8;
                } else if (this.current.anInt4067 == 0) {
                    headerSize = 1;
                }

                int var6;
                int readBytes;
                int var8;
                if (headerSize <= 0) {
                    var6 = this.current.data.buffer.length - this.current.padding;
                    readBytes = 512 - this.current.anInt4067;
                    if (readBytes > var6 - this.current.data.index) {
                        readBytes = var6 - this.current.data.index;
                    }

                    if (available < readBytes) {
                        readBytes = available;
                    }

                    this.connection.readBytes(this.current.data.buffer, this.current.data.index, readBytes);
                    if (this.aByte1009 != 0) {
                        for (var8 = 0; var8 < readBytes; ++var8) {
                            this.current.data.buffer[this.current.data.index - -var8] = (byte) Unsorted.bitwiseXOR(this.current.data.buffer[this.current.data.index + var8], this.aByte1009);
                        }
                    }

                    this.current.anInt4067 += readBytes;
                    this.current.data.index += readBytes;
                    if (this.current.data.index == var6) {
                        this.current.unlinkNode();
                        this.current.waiting = false;
                        this.current = null;
                    } else if (this.current.anInt4067 == 512) {
                        this.current.anInt4067 = 0;
                    }
                } else {
                    var6 = headerSize - this.incoming.index;
                    if (available < var6) {
                        var6 = available;
                    }

                    this.connection.readBytes(this.incoming.buffer, this.incoming.index, var6);
                    if (0 != this.aByte1009) {
                        for (readBytes = 0; readBytes < var6; ++readBytes) {
                            this.incoming.buffer[readBytes + this.incoming.index] = (byte) Unsorted.bitwiseXOR(this.incoming.buffer[readBytes + this.incoming.index], this.aByte1009);
                        }
                    }

                    this.incoming.index += var6;
                    if (headerSize <= this.incoming.index) {
                        if (this.current == null) {
                            this.incoming.index = 0;
                            readBytes = this.incoming.readUnsignedByte();
                            var8 = this.incoming.readUnsignedShort();
                            int var9 = this.incoming.readUnsignedByte();
                            int var10 = this.incoming.readInt();
                            int var11 = 127 & var9;
                            boolean var12 = (var9 & 128) != 0;
                            Js5ResourceRequest var15;
                            long var13 = (readBytes << 16) - -var8;
                            if (var12) {
                                for (var15 = this.pendingNormalRequests.getFront(); null != var15 && var13 != var15.nodeKey; var15 = this.pendingNormalRequests.next()) {
                                }
                            } else {
                                for (var15 = this.pendingPriorityRequests.getFront(); var15 != null && var15.nodeKey != var13; var15 = this.pendingPriorityRequests.next()) {
                                }
                            }

                            if (null == var15) {
                                throw new IOException("Could not find cache file " + readBytes + ", " + var8 + "!");
                            }

                            int var16 = var11 != 0 ? 9 : 5;
                            this.current = var15;
                            this.current.data = new DataBuffer(var10 - (-var16 - this.current.padding));
                            this.current.data.writeByte(var11);
                            this.current.data.writeInt(var10);
                            this.current.anInt4067 = 8;
                            this.incoming.index = 0;
                        } else {
                            if (this.current.anInt4067 != 0) {
                                throw new IOException();
                            }

                            if (this.incoming.buffer[0] == -1) {
                                this.current.anInt4067 = 1;
                                this.incoming.index = 0;
                            } else {
                                this.current = null;
                            }
                        }
                    }
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                this.connection.close();
            } catch (Exception var17) {
            }

            this.status = -2;
            this.errors++;
            this.connection = null;
            return this.countPriorityRequests() == 0 && this.countNormalRequests() == 0;
        }
    }

    public final void requestTermination() {
        if (this.connection == null) {
            return;
        }

        try {
            this.outgoing.index = 0;

            this.outgoing.writeByte(7);
            this.outgoing.writeMedium(0);
            this.connection.sendBytes(this.outgoing.buffer, 4);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                this.connection.close();
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            this.errors++;
            this.status = -2;
            this.connection = null;
        }
    }

    private int countNormalRequests() {
        return this.normalRequests.size() - -this.pendingNormalRequests.size();
    }

    public final void sendLoginState(boolean loggedIn) {
        if (this.connection == null) {
            return;
        }

        try {
            this.outgoing.index = 0;
            this.outgoing.writeByte(loggedIn ? 2 : 3);
            this.outgoing.writeMedium(0);
            this.connection.sendBytes(this.outgoing.buffer, 4);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                this.connection.close();
            } catch (Exception var5) {
            }

            this.errors++;
            this.status = -2;
            this.connection = null;
        }
    }

    public final void applyDummyStreams() {
        if (this.connection != null) {
            this.connection.applyDummyStreams();
        }
    }

    public final void connect(boolean var1, Connection connection) {
        if (null != this.connection) {
            try {
                this.connection.close();
            } catch (Exception var8) {
            }

            this.connection = null;
        }

        this.connection = connection;
        this.sendMagic();
        this.sendLoginState(var1);
        this.incoming.index = 0;
        this.current = null;

        while (true) {
            Js5ResourceRequest var4 = this.pendingPriorityRequests.poll();
            if (null == var4) {
                while (true) {
                    var4 = this.pendingNormalRequests.poll();
                    if (var4 == null) {
                        if (this.aByte1009 != 0) {
                            try {
                                this.outgoing.index = 0;
                                this.outgoing.writeByte(4);
                                this.outgoing.writeByte(this.aByte1009);
                                this.outgoing.writeShort(0);
                                this.connection.sendBytes(this.outgoing.buffer, 4);
                            } catch (IOException var7) {
                                var7.printStackTrace();
                                try {
                                    this.connection.close();
                                } catch (Exception var6) {
                                }

                                this.status = -2;
                                ++this.errors;
                                this.connection = null;
                            }
                        }

                        this.latency = 0;
                        this.lastAttempt = TimeUtils.time();
                        return;
                    }

                    this.normalRequests.offer(var4);
                }
            }

            this.priorityRequests.offer(var4);
        }
    }

    public final boolean priorityRequestsFull() {
        return 20 <= this.countPriorityRequests();
    }

    public final void closeWithError() {
        try {
            this.connection.close();
        } catch (Exception var4) {
        }

        this.status = -1;
        this.aByte1009 = (byte) ((int) (255.0D * Math.random() + 1.0D));
        this.connection = null;
        this.errors++;
    }

    public final int countPriorityRequests() {
        return this.priorityRequests.size() - -this.pendingPriorityRequests.size();
    }

    public final void close() {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    public final Js5ResourceRequest request(int index, int archive, byte padding, boolean priority) {
        Js5ResourceRequest request = new Js5ResourceRequest();
        long key = archive + (index << 16);
        request.priority = priority;
        request.nodeKey = key;
        request.padding = padding;

        if (priority) {
            if (this.countPriorityRequests() >= 20) {
                throw new RuntimeException("Priority Js5 request queue full");
            }

            this.priorityRequests.offer(request);
        } else {
            if (this.countNormalRequests() >= 20) {
                throw new RuntimeException("Normal Js5 request queue full");
            }

            this.normalRequests.offer(request);
        }

        return request;
    }

    private void sendMagic() {
        if (connection == null) return;

        try {
            this.outgoing.index = 0;
            this.outgoing.writeByte(6);
            this.outgoing.writeMedium(3);
            this.connection.sendBytes(this.outgoing.buffer, 4);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                this.connection.close();
            } catch (Exception var4) {
            }

            this.errors++;
            this.connection = null;
            this.status = -2;
        }
    }

}
