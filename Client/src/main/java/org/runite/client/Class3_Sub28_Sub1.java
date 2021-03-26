package org.runite.client;

import org.rs09.client.Node;

import java.awt.*;

public final class Class3_Sub28_Sub1 extends Node {

    static boolean aBoolean3531 = false;
    static Class3_Sub20 aClass3_Sub20_3532 = new Class3_Sub20(0, 0);
    int[] anIntArray3533;
    int[] anIntArray3534;
    int[] anIntArray3535;
    static int anInt3536;
    RSString quickChatMenu;
    static int anInt3539;
    static int dropAction;
    static int counter;
    int[] anIntArray3540;


    final void method525() {
        try {
            int var2;
            if (null != this.anIntArray3540) {
                for (var2 = 0; var2 < this.anIntArray3540.length; ++var2) {
                    this.anIntArray3540[var2] = TextureOperation3.bitwiseOr(this.anIntArray3540[var2], 32768);
                }
            }

            if (null != this.anIntArray3534) {
                for (var2 = 0; this.anIntArray3534.length > var2; ++var2) {
                    this.anIntArray3534[var2] = TextureOperation3.bitwiseOr(this.anIntArray3534[var2], 32768);
                }
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "bc.O(" + -85 + ')');
        }
    }

    final int method526(int var1) {
        try {
            if (this.anIntArray3540 != null) {
                for (int var3 = 0; this.anIntArray3540.length > var3; ++var3) {
                    if (var1 == this.anIntArray3533[var3]) {
                        return this.anIntArray3540[var3];
                    }
                }

            }
            return -1;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "bc.Q(" + var1 + ',' + 0 + ')');
        }
    }

    private void method527(DataBuffer buffer, int var3) {
        try {

            if (var3 == 1) {
                this.quickChatMenu = buffer.readString();
            } else {
                int var4;
                int var5;
                if (var3 == 2) {
                    var4 = buffer.readUnsignedByte();
                    this.anIntArray3534 = new int[var4];
                    this.anIntArray3535 = new int[var4];

                    for (var5 = 0; var5 < var4; ++var5) {
                        this.anIntArray3534[var5] = buffer.readUnsignedShort();
                        this.anIntArray3535[var5] = TextureOperation29.method322(buffer.readSignedByte());
                    }
                } else if (var3 == 3) {
                    var4 = buffer.readUnsignedByte();
                    this.anIntArray3540 = new int[var4];
                    this.anIntArray3533 = new int[var4];

                    for (var5 = 0; var5 < var4; ++var5) {
                        this.anIntArray3540[var5] = buffer.readUnsignedShort();
                        this.anIntArray3533[var5] = TextureOperation29.method322(buffer.readSignedByte());
                    }
                }
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "bc.E(" + (buffer != null ? "{...}" : "null") + ',' + 0 + ',' + var3 + ')');
        }
    }

    final int method529(int var2) {
        try {
            if (null != this.anIntArray3534) {
                for (int var4 = 0; this.anIntArray3534.length > var4; ++var4) {
                    if (var2 == this.anIntArray3535[var4]) {
                        return this.anIntArray3534[var4];
                    }
                }

            }
            return -1;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "bc.P(" + (byte) 50 + ',' + var2 + ')');
        }
    }

    final void method530(DataBuffer buffer) {
        try {

            while (true) {
                int opcode = buffer.readUnsignedByte();
                if (opcode == 0) {
                    return;
                }

                this.method527(buffer, opcode);
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "bc.D(" + (buffer != null ? "{...}" : "null") + ',' + (byte) 116 + ')');
        }
    }

}
