package org.runite.client;

import org.rs09.client.Linkable;
import org.rs09.client.data.ReferenceCache;

public final class LinkedList {

    static ReferenceCache aReferenceCache_939 = new ReferenceCache(4);
    Linkable aClass3_940 = new Linkable();
    private Linkable aClass3_941;


    public LinkedList() {
        try {
            this.aClass3_940.previous = this.aClass3_940;
            this.aClass3_940.next = this.aClass3_940;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "ih.<init>()");
        }
    }

    static Class70 method1209(int var0, int var1, int var2) {
        Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
        if (var3 == null) {
            return null;
        } else {
            Class70 var4 = var3.aClass70_2234;
            var3.aClass70_2234 = null;
            return var4;
        }
    }

    static Class3_Sub28_Sub9 method1210(int var1) {
        try {
            Class3_Sub28_Sub9 var2 = (Class3_Sub28_Sub9) Class163.aClass47_2041.get(var1);

            if (null == var2) {
                byte[] var3 = TextureOperation27.aClass153_3098.getFile(11, var1);
                var2 = new Class3_Sub28_Sub9();
                if (var3 != null) {
                    var2.method583(new DataBuffer(var3));
                }

                Class163.aClass47_2041.put(var1, var2);
            }
            return var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ih.G(" + 64 + ',' + var1 + ')');
        }
    }

    static void method1213(int var0, Class3_Sub11[] var1) {
        Class3_Sub23.aClass3_Sub11ArrayArray2542[var0] = var1;
    }

    static void method1214(int var0, int var1, int var2, int var3) {
        try {
            Class3_Sub9 var5;
            for (var5 = (Class3_Sub9) Unsorted.aLinkedList_78.method1222(); var5 != null; var5 = (Class3_Sub9) Unsorted.aLinkedList_78.method1221()) {
                Unsorted.method606(var1, var5, var3, var0, var2, 126);
            }

            byte var6;
            RenderAnimationDefinition var7;
            int var8;
            for (var5 = (Class3_Sub9) Unsorted.aLinkedList_1242.method1222(); var5 != null; var5 = (Class3_Sub9) Unsorted.aLinkedList_1242.method1221()) {
                var6 = 1;
                var7 = var5.aClass140_Sub4_Sub2_2324.getRenderAnimationType();
                if (var5.aClass140_Sub4_Sub2_2324.anInt2764 == var7.stand_animation) {
                    var6 = 0;
                } else if (var5.aClass140_Sub4_Sub2_2324.anInt2764 != var7.run_anim && var5.aClass140_Sub4_Sub2_2324.anInt2764 != var7.run_follow_full_turn_anim && var5.aClass140_Sub4_Sub2_2324.anInt2764 != var7.run_follow_cw_turn_anim && var5.aClass140_Sub4_Sub2_2324.anInt2764 != var7.run_follow_ccw_turn_anim) {
                    if (var7.slow_walk_anim == var5.aClass140_Sub4_Sub2_2324.anInt2764 || var7.slow_walk_follow_full_turn_anim == var5.aClass140_Sub4_Sub2_2324.anInt2764 || var5.aClass140_Sub4_Sub2_2324.anInt2764 == var7.slow_walk_follow_cw_turn_anim || var5.aClass140_Sub4_Sub2_2324.anInt2764 == var7.slow_walk_follow_ccw_turn_anim) {
                        var6 = 3;
                    }
                } else {
                    var6 = 2;
                }

                if (var5.anInt2322 != var6) {
                    var8 = Class70.method1232(var5.aClass140_Sub4_Sub2_2324);
                    if (var8 != var5.anInt2332) {
                        if (var5.aClass3_Sub24_Sub1_2312 != null) {
                            Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var5.aClass3_Sub24_Sub1_2312);
                            var5.aClass3_Sub24_Sub1_2312 = null;
                        }

                        var5.anInt2332 = var8;
                    }

                    var5.anInt2322 = var6;
                }

                var5.anInt2326 = var5.aClass140_Sub4_Sub2_2324.xAxis;
                var5.anInt2321 = var5.aClass140_Sub4_Sub2_2324.xAxis + var5.aClass140_Sub4_Sub2_2324.getSize() * 64;
                var5.anInt2308 = var5.aClass140_Sub4_Sub2_2324.zAxis;
                var5.anInt2307 = var5.aClass140_Sub4_Sub2_2324.zAxis + var5.aClass140_Sub4_Sub2_2324.getSize() * 64;
                Unsorted.method606(var1, var5, var3, var0, var2, 1 ^ 113);
            }

            for (var5 = (Class3_Sub9) Unsorted.aHashTable_4046.first(); var5 != null; var5 = (Class3_Sub9) Unsorted.aHashTable_4046.next()) {
                var6 = 1;
                var7 = var5.aClass140_Sub4_Sub1_2327.getRenderAnimationType();
                if (var5.aClass140_Sub4_Sub1_2327.anInt2764 == var7.stand_animation) {
                    var6 = 0;
                } else if (var5.aClass140_Sub4_Sub1_2327.anInt2764 != var7.run_anim && var5.aClass140_Sub4_Sub1_2327.anInt2764 != var7.run_follow_full_turn_anim && var7.run_follow_cw_turn_anim != var5.aClass140_Sub4_Sub1_2327.anInt2764 && var7.run_follow_ccw_turn_anim != var5.aClass140_Sub4_Sub1_2327.anInt2764) {
                    if (var7.slow_walk_anim == var5.aClass140_Sub4_Sub1_2327.anInt2764 || var5.aClass140_Sub4_Sub1_2327.anInt2764 == var7.slow_walk_follow_full_turn_anim || var7.slow_walk_follow_cw_turn_anim == var5.aClass140_Sub4_Sub1_2327.anInt2764 || var7.slow_walk_follow_ccw_turn_anim == var5.aClass140_Sub4_Sub1_2327.anInt2764) {
                        var6 = 3;
                    }
                } else {
                    var6 = 2;
                }

                if (var6 != var5.anInt2322) {
                    var8 = Class81.method1398(var5.aClass140_Sub4_Sub1_2327);
                    if (var8 != var5.anInt2332) {
                        if (var5.aClass3_Sub24_Sub1_2312 != null) {
                            Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var5.aClass3_Sub24_Sub1_2312);
                            var5.aClass3_Sub24_Sub1_2312 = null;
                        }

                        var5.anInt2332 = var8;
                    }

                    var5.anInt2322 = var6;
                }

                var5.anInt2326 = var5.aClass140_Sub4_Sub1_2327.xAxis;
                var5.anInt2321 = var5.aClass140_Sub4_Sub1_2327.xAxis + 64 * var5.aClass140_Sub4_Sub1_2327.getSize();
                var5.anInt2308 = var5.aClass140_Sub4_Sub1_2327.zAxis;
                var5.anInt2307 = var5.aClass140_Sub4_Sub1_2327.zAxis + var5.aClass140_Sub4_Sub1_2327.getSize() * 64;
                Unsorted.method606(var1, var5, var3, var0, var2, 110);
            }

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "ih.K(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + 1 + ')');
        }
    }

    static RSString method1218(int var2) {
        try {
            return Unsorted.method1723((byte) -128, true, var2);
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ih.A(" + true + ',' + 127 + ',' + var2 + ')');
        }
    }

    final void method1211(int var1) {
        try {
            while (true) {
                Linkable var2 = this.aClass3_940.next;
                if (var2 == this.aClass3_940) {
                    if (var1 > -47) {
                        this.aClass3_940 = null;
                    }

                    this.aClass3_941 = null;
                    return;
                }

                var2.unlink();
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ih.C(" + var1 + ')');
        }
    }

    final Linkable method1212() {
        try {
            Linkable var2 = this.aClass3_940.previous;

            if (this.aClass3_940 == var2) {
                this.aClass3_941 = null;
                return null;
            } else {
                this.aClass3_941 = var2.previous;
                return var2;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ih.B(" + 2 + ')');
        }
    }

    final void method1215(Linkable var2) {
        try {
            if (null != var2.previous) {
                var2.unlink();
            }

            var2.next = this.aClass3_940;
            var2.previous = this.aClass3_940.previous;
            var2.previous.next = var2;
            var2.next.previous = var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ih.D(" + true + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    final void method1216(Linkable var2) {
        try {
            if (null != var2.previous) {
                var2.unlink();
            }

            var2.next = this.aClass3_940.next;
            var2.previous = this.aClass3_940;
            var2.previous.next = var2;
            var2.next.previous = var2;

        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ih.N(" + 64 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    final Linkable method1219(int var1) {
        try {
            if (var1 < 13) {
                this.aClass3_940 = null;
            }

            Linkable var2 = this.aClass3_941;
            if (this.aClass3_940 == var2) {
                this.aClass3_941 = null;
                return null;
            } else {
                this.aClass3_941 = var2.previous;
                return var2;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ih.H(" + var1 + ')');
        }
    }

    final Linkable method1220() {
        try {
            Linkable var2 = this.aClass3_940.next;
            if (this.aClass3_940 == var2) {
                return null;
            } else {
                var2.unlink();
                return var2;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ih.E(" + (byte) -3 + ')');
        }
    }

    final Linkable method1221() {
        try {
            Linkable var2 = this.aClass3_941;
            if (var2 == this.aClass3_940) {
                this.aClass3_941 = null;
                return null;
            } else {
                this.aClass3_941 = var2.next;
                return var2;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ih.L(" + 1 + ')');
        }
    }

    final Linkable method1222() {
        try {
            Linkable var2 = this.aClass3_940.next;
            if (this.aClass3_940 == var2) {
                this.aClass3_941 = null;
                return null;
            } else {
                this.aClass3_941 = var2.next;
                return var2;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ih.F(" + 1 + ')');
        }
    }

}
