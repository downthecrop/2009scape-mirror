package org.runite.client;


import net.arikia.dev.drpc.DiscordRPC;
import org.rs09.Discord;
import org.rs09.SystemLogger;
import org.rs09.client.config.GameConfig;
import org.rs09.client.console.DeveloperConsole;
import org.rs09.client.constants.gametype.MechscapeGameAppearance;
import org.rs09.client.constants.gametype.RunescapeGameAppearance;
import org.rs09.client.data.HashTable;
import org.rs09.client.filestore.resources.configs.enums.EnumDefinitionProvider;
import org.rs09.client.filestore.resources.configs.structs.StructDefinitionProvider;
import org.rs09.client.net.Connection;
import org.runite.client.drawcalls.LoadingBox;
import org.runite.client.drawcalls.StartupLoadingBarInitial;
import org.runite.client.drawcalls.StartupLoadingBar;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import static org.rs09.client.constants.Parameter.*;

public final class Client extends GameShell {

    /**
     *
     */
    private static final long serialVersionUID = 8336806252605101745L;
    public static int messageToDisplay = -2;
    public static RSString loadingBarTextToDisplay = RSString.parse("");
    static HashTable aHashTable_2194 = new HashTable(16);
    static Class3_Sub11[][] aClass3_Sub11ArrayArray2199;
    static int[] anIntArray2200;
    static int ZOOM = 600;
    static int currentPort;
    static int rectDebugInt = 0;
    public static int LoadingStageNumber = 10;
    static int anInt869;
    static int loginScreenInterfaceID;
    static int anInt2275 = 1;
    static int anInt2317 = 0;
    static int anInt3068 = 0;
    static int[] anIntArray3288 = new int[]{4, 4, 1, 2, 6, 4, 2, 49, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    static long aLong3296 = 0L;
    static Class3_Sub24_Sub4 aClass3_Sub24_Sub4_1193;
    static long aLong1310 = 0L;
    static int anInt1354 = 0;
    static LinkedList aLinkedList_1471 = new LinkedList();
    static Class30 aClass30_1572;
    static int[] anIntArray3780 = new int[32];
    static boolean paramAdvertisementSuppressed = false;
    static int anInt3773;
    static Client clientInstance;
    static boolean sweepReferenceCache = false;

    static void method631(CacheIndex var1) {
        try {
            Class3_Sub28_Sub5.aClass153_3580 = var1;
            anInt869 = Class3_Sub28_Sub5.aClass153_3580.getFileAmount(4);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "qc.D(" + false + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static void method1175(int var0) {
        try {
            TextureOperation13.anInt3362 = -1;
            Texture.anInt1150 = -1;

            Class3_Sub28_Sub1.anInt3536 = var0;
            Class3_Sub5.method117();
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "hj.D(" + var0 + ',' + 112 + ')');
        }
    }

    static void method375(CacheIndex var1, CacheIndex var2) {
        try {
            Class24.aClass153_152 = var1;
            LoginHandler.aClass153_1680 = var2;
            Class25.anInt497 = LoginHandler.aClass153_1680.getFileAmount(3);
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "jl.D(" + 3 + ',' + (var1 != null ? "{...}" : "null") + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    static void invalidArgument(String var0) {
        try {
            System.out.println("Bad " + var0 + ", Usage: worldid, <live/rc/wip>, <english/german>, <game0/game1>");
            System.exit(1);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "kd.O(" + (var0 != null ? "{...}" : "null") + ',' + (byte) 38 + ')');
        }
    }

    static RSInterface method42(RSInterface var0) {
        int var1 = method44(var0).method94();
        if (var1 == 0) {
            return null;
        } else {
            for (int var2 = 0; var2 < var1; ++var2) {
                var0 = Unsorted.getRSInterface(var0.parentId);
                if (var0 == null) {
                    return null;
                }
            }

            return var0;
        }
    }

    static Class3_Sub1 method44(RSInterface var0) {
        Class3_Sub1 var1 = (Class3_Sub1) Class124.aHashTable_1659.get(((long) var0.componentHash << 32) + (long) var0.anInt191);
        return var1 != null ? var1 : var0.aClass3_Sub1_257;
    }

    /**
     * Client Initialization
     * <p>
     * Client command line initialization (parameter modeWhere 3)
     * NOTE: Attempting to run the client this way puts paramModeWhere into LOCAL_ENVIRONMENT mode
     * All of these arguments can be set through "program arguments"
     */
    public static void main(String[] args) {
        try {
            if (args.length != 4) {
                invalidArgument("argument count");
            }

            int language = -1;

            ObjectDefinition.paramWorldID = Integer.parseInt(args[0]);

            Class44.paramModeWhere = LOCAL_ENVIRONMENT;

            switch (args[1]) {
                case "live":
                    TextureOperation20.paramModeWhat = LIVE_SERVER;
                    break;
                case "rc":
                    TextureOperation20.paramModeWhat = RELEASE_CANDIDATE;
                    break;
                case "wip":
                    TextureOperation20.paramModeWhat = WORK_IN_PROGRESS;
                    break;
                default:
                    invalidArgument("modewhat");
                    break;
            }

            paramAdvertisementSuppressed = false;

            try {
                byte[] languageSelection = args[2].getBytes(StandardCharsets.ISO_8859_1);
                    /*
                        Accepts en, de, fr, pt
                     */
                language = TextureOperation15.compareEnteredLanguageArgument(TextureOperation33.bufferToString(languageSelection, languageSelection.length, 0));
            } catch (Exception ignored) {

            }

            if (language == -1) {
                switch (args[2]) {
                    case "english":
                        Class3_Sub20.paramLanguage = LANGUAGE_ENGLISH;
                        break;
                    case "german":
                        Class3_Sub20.paramLanguage = LANGUAGE_GERMAN;
                        break;
                    case "french":
                        Class3_Sub20.paramLanguage = LANGUAGE_FRENCH;
                        break;
                    default:
                        invalidArgument("language");
                        break;
                }
            } else {
                Class3_Sub20.paramLanguage = language;
            }

            Unsorted.languageSetter(Class3_Sub20.paramLanguage);

            Class163_Sub2_Sub1.paramObjectTagEnabled = false;
            Unsorted.paramJavaScriptEnabled = false;

            switch (args[3]) {
                case "game0":
                    Class158.paramGameTypeID = GAME_TYPE_RUNESCAPE;
                    break;
                case "game1":
                    Class158.paramGameTypeID = GAME_TYPE_MECHSCAPE;
                    break;
                default:
                    invalidArgument("game");
                    break;
            }

            Class3_Sub31.paramCountryID = 0;

            Class106.paramUserUsingInternetExplorer = false;

            Class3_Sub26.paramAffid = NO_AFFILIATE;

            Class163_Sub2.paramSettings = RSString.parse("");

            Client client = new Client();
            clientInstance = client;
            client.launch();
            GameShell.frame.setLocation(40, 40);

        } catch (Exception var4) {
            Class49.reportError(null, var4);
        }
    }

    static void handleItemSwitch(RSInterface[] interfaces, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        for (RSInterface inter : interfaces) {
            if (inter != null && inter.parentId == var1 && (!inter.usingScripts || inter.type == 0 || inter.aBoolean195 || method44(inter).anInt2205 != 0 || inter == PacketParser.aClass11_88 || inter.anInt189 == 1338) && (!inter.usingScripts || !method51(inter))) {
                int var10 = inter.anInt306 + var6;
                int var11 = inter.anInt210 + var7;
                int var12;
                int var13;
                int var14;
                int var15;
                if (inter.type == 2) {
                    var12 = var2;
                    var13 = var3;
                    var14 = var4;
                    var15 = var5;
                } else {
                    int var16 = var10 + inter.width;
                    int var17 = var11 + inter.height;
                    if (inter.type == 9) {
                        ++var16;
                        ++var17;
                    }

                    var12 = var10 > var2 ? var10 : var2;
                    var13 = var11 > var3 ? var11 : var3;
                    var14 = var16 < var4 ? var16 : var4;
                    var15 = var17 < var5 ? var17 : var5;
                }

                if (inter == Class56.aClass11_886) {
                    Class21.aBoolean440 = true;
                    Class3_Sub15.anInt2421 = var10;
                    Class3_Sub2.anInt2218 = var11;
                }

                if (!inter.usingScripts || var12 < var14 && var13 < var15) {
                    if (inter.type == 0) {
                        if (!inter.usingScripts && method51(inter) && Class107.aClass11_1453 != inter) {
                            continue;
                        }

                        if (inter.aBoolean219 && Class126.anInt1676 >= var12 && Unsorted.anInt1709 >= var13 && Class126.anInt1676 < var14 && Unsorted.anInt1709 < var15) {
                            for (CS2Script var27 = (CS2Script) aLinkedList_1471.method1222(); var27 != null; var27 = (CS2Script) aLinkedList_1471.method1221()) {
                                if (var27.aBoolean2446) {
                                    var27.unlink();
                                    var27.aClass11_2449.aBoolean163 = false;
                                }
                            }

                            if (Class75_Sub3.anInt2658 == 0) {
                                Class56.aClass11_886 = null;
                                PacketParser.aClass11_88 = null;
                            }

                            Class3_Sub19.anInt2475 = 0;
                        }
                    }

                    if (inter.usingScripts) {
                        boolean var26;
                        var26 = Class126.anInt1676 >= var12 && Unsorted.anInt1709 >= var13 && Class126.anInt1676 < var14 && Unsorted.anInt1709 < var15;

                        boolean var25 = false;
                        if (TextureOperation21.anInt3069 == 1 && var26) {
                            var25 = true;
                        }

                        boolean var18 = false;
                        if (Unsorted.anInt3644 == 1 && Class163_Sub1.anInt2993 >= var12 && Class38_Sub1.anInt2614 >= var13 && Class163_Sub1.anInt2993 < var14 && Class38_Sub1.anInt2614 < var15) {
                            var18 = true;
                        }

                        int var19;
                        int var21;
                        if (inter.aByteArray263 != null) {
                            for (var19 = 0; var19 < inter.aByteArray263.length; ++var19) {
                                if (!ObjectDefinition.aBooleanArray1490[inter.aByteArray263[var19]]) {
                                    if (inter.anIntArray310 != null) {
                                        inter.anIntArray310[var19] = 0;
                                    }
                                } else if (inter.anIntArray310 == null || Class44.anInt719 >= inter.anIntArray310[var19]) {
                                    byte var20 = inter.aByteArray231[var19];
                                    if (var20 == 0 || ((var20 & 2) == 0 || ObjectDefinition.aBooleanArray1490[86]) && ((var20 & 1) == 0 || ObjectDefinition.aBooleanArray1490[82]) && ((var20 & 4) == 0 || ObjectDefinition.aBooleanArray1490[81])) {
                                        Unsorted.method66(RSString.parse(""), -1, var19 + 1, (byte) -29, inter.componentHash);
                                        var21 = inter.anIntArray299[var19];
                                        if (inter.anIntArray310 == null) {
                                            inter.anIntArray310 = new int[inter.aByteArray263.length];
                                        }

                                        if (var21 == 0) {
                                            inter.anIntArray310[var19] = Integer.MAX_VALUE;
                                        } else {
                                            inter.anIntArray310[var19] = Class44.anInt719 + var21;
                                        }
                                    }
                                }
                            }
                        }

                        if (var18) {
                            InterfaceWidget.a(Class38_Sub1.anInt2614 - var11, Class163_Sub1.anInt2993 - var10, 97, inter);
                        }

                        if (Class56.aClass11_886 != null && Class56.aClass11_886 != inter && var26 && (method44(inter).method98() || (inter.componentHash == 49938505 && Class56.aClass11_886.componentHash == 49938505))) {
                            Class27.aClass11_526 = inter;
                        }
                        if (inter == PacketParser.aClass11_88) {
                            Class85.aBoolean1167 = true;
                            TextureOperation20.anInt3156 = var10;
                            Class134.anInt1761 = var11;
                        }

                        if (inter.aBoolean195 || inter.anInt189 != 0) {
                            CS2Script var30;
                            if (var26 && Class29.anInt561 != 0 && inter.anObjectArray183 != null) {
                                var30 = new CS2Script();
                                var30.aBoolean2446 = true;
                                var30.aClass11_2449 = inter;
                                var30.scrollbarScrollAmount = Class29.anInt561;
                                var30.arguments = inter.anObjectArray183;
                                aLinkedList_1471.method1215(var30);
                            }

                            if (Class56.aClass11_886 != null || Class67.aClass11_1017 != null || Class38_Sub1.aBoolean2615 || inter.anInt189 != 1400 && Class3_Sub19.anInt2475 > 0) {
                                var18 = false;
                                var25 = false;
                                var26 = false;
                            }

                            int var29;
                            if (inter.anInt189 != 0) {
                                if (inter.anInt189 == 1337) {
                                    Class168.aClass11_2091 = inter;
                                    Class20.method909(inter);
                                    continue;
                                }

                                if (inter.anInt189 == 1338) {
                                    if (var18) {
                                        Class1.anInt56 = Class163_Sub1.anInt2993 - var10;
                                        Class58.anInt916 = Class38_Sub1.anInt2614 - var11;
                                    }
                                    continue;
                                }

                                if (inter.anInt189 == 1400) {
                                    Class3_Sub28_Sub3.aClass11_3551 = inter;
                                    if (var18) {
                                        if (ObjectDefinition.aBooleanArray1490[82] && Player.rights > 0) {
                                            var19 = (int) ((double) (Class163_Sub1.anInt2993 - var10 - inter.width / 2) * 2.0D / (double) Class44.aFloat727);
                                            var29 = (int) ((double) (Class38_Sub1.anInt2614 - var11 - inter.height / 2) * 2.0D / (double) Class44.aFloat727);
                                            var21 = Class3_Sub28_Sub1.anInt3536 + var19;
                                            int var32 = Class3_Sub4.anInt2251 + var29;
                                            int var23 = var21 + TextureOperation37.anInt3256;
                                            int var24 = Class108.anInt1460 - 1 - var32 + Unsorted.anInt65;
                                            Class30.method979(var23, var24, 0);
                                            TextureOperation4.method264((byte) 126);
                                            continue;
                                        }

                                        Class3_Sub19.anInt2475 = 1;
                                        Unsorted.anInt1881 = Class126.anInt1676;
                                        Class95.anInt1336 = Unsorted.anInt1709;
                                        continue;
                                    }

                                    if (var25 && Class3_Sub19.anInt2475 > 0) {
                                        if (Class3_Sub19.anInt2475 == 1 && (Unsorted.anInt1881 != Class126.anInt1676 || Class95.anInt1336 != Unsorted.anInt1709)) {
                                            Unsorted.anInt4073 = Class3_Sub28_Sub1.anInt3536;
                                            Class38.anInt660 = Class3_Sub4.anInt2251;
                                            Class3_Sub19.anInt2475 = 2;
                                        }

                                        if (Class3_Sub19.anInt2475 == 2) {
                                            method1175(Unsorted.anInt4073 + (int) ((double) (Unsorted.anInt1881 - Class126.anInt1676) * 2.0D / (double) NPC.aFloat3979));
                                            TextureOperation8.method354(Class38.anInt660 + (int) ((double) (Class95.anInt1336 - Unsorted.anInt1709) * 2.0D / (double) NPC.aFloat3979));
                                        }
                                        continue;
                                    }

                                    Class3_Sub19.anInt2475 = 0;
                                    continue;
                                }

                                if (inter.anInt189 == 1401) {
                                    if (var25) {
                                        TextureOperation23.method253(inter.width, Unsorted.anInt1709 - var11, Class126.anInt1676 - var10, inter.height);
                                    }
                                    continue;
                                }

                                if (inter.anInt189 == 1402) {
                                    if (!HDToolKit.highDetail) {
                                        Class20.method909(inter);
                                    }
                                    continue;
                                }
                            }

                            if (!inter.aBoolean188 && var18) {
                                inter.aBoolean188 = true;
                                if (inter.anObjectArray165 != null) {
                                    var30 = new CS2Script();
                                    var30.aBoolean2446 = true;
                                    var30.aClass11_2449 = inter;
                                    var30.worldSelectCursorPositionX = Class163_Sub1.anInt2993 - var10;
                                    var30.scrollbarScrollAmount = Class38_Sub1.anInt2614 - var11;
                                    var30.arguments = inter.anObjectArray165;
                                    aLinkedList_1471.method1215(var30);
                                }
                            }

                            if (inter.aBoolean188 && var25 && inter.anObjectArray170 != null) {
                                var30 = new CS2Script();
                                var30.aBoolean2446 = true;
                                var30.aClass11_2449 = inter;
                                var30.worldSelectCursorPositionX = Class126.anInt1676 - var10;
                                var30.scrollbarScrollAmount = Unsorted.anInt1709 - var11;
                                var30.arguments = inter.anObjectArray170;
                                aLinkedList_1471.method1215(var30);
                            }

                            if (inter.aBoolean188 && !var25) {
                                inter.aBoolean188 = false;
                                if (inter.anObjectArray239 != null) {
                                    var30 = new CS2Script();
                                    var30.aBoolean2446 = true;
                                    var30.aClass11_2449 = inter;
                                    var30.worldSelectCursorPositionX = Class126.anInt1676 - var10;
                                    var30.scrollbarScrollAmount = Unsorted.anInt1709 - var11;
                                    var30.arguments = inter.anObjectArray239;
                                    Class65.aLinkedList_983.method1215(var30);
                                }
                            }

                            if (var25 && inter.anObjectArray180 != null) {
                                var30 = new CS2Script();
                                var30.aBoolean2446 = true;
                                var30.aClass11_2449 = inter;
                                var30.worldSelectCursorPositionX = Class126.anInt1676 - var10;
                                var30.scrollbarScrollAmount = Unsorted.anInt1709 - var11;
                                var30.arguments = inter.anObjectArray180;
                                aLinkedList_1471.method1215(var30);
                            }

                            if (!inter.aBoolean163 && var26) {
                                inter.aBoolean163 = true;
                                if (inter.anObjectArray248 != null) {
                                    var30 = new CS2Script();
                                    var30.aBoolean2446 = true;
                                    var30.aClass11_2449 = inter;
                                    var30.worldSelectCursorPositionX = Class126.anInt1676 - var10;
                                    var30.scrollbarScrollAmount = Unsorted.anInt1709 - var11;
                                    var30.arguments = inter.anObjectArray248;
                                    aLinkedList_1471.method1215(var30);
                                }
                            }

                            if (inter.aBoolean163 && var26 && inter.anObjectArray276 != null) {
                                var30 = new CS2Script();
                                var30.aBoolean2446 = true;
                                var30.aClass11_2449 = inter;
                                var30.worldSelectCursorPositionX = Class126.anInt1676 - var10;
                                var30.scrollbarScrollAmount = Unsorted.anInt1709 - var11;
                                var30.arguments = inter.anObjectArray276;
                                aLinkedList_1471.method1215(var30);
                            }

                            if (inter.aBoolean163 && !var26) {
                                inter.aBoolean163 = false;
                                if (inter.anObjectArray281 != null) {
                                    var30 = new CS2Script();
                                    var30.aBoolean2446 = true;
                                    var30.aClass11_2449 = inter;
                                    var30.worldSelectCursorPositionX = Class126.anInt1676 - var10;
                                    var30.scrollbarScrollAmount = Unsorted.anInt1709 - var11;
                                    var30.arguments = inter.anObjectArray281;
                                    Class65.aLinkedList_983.method1215(var30);
                                }
                            }

                            if (inter.anObjectArray269 != null) {
                                var30 = new CS2Script();
                                var30.aClass11_2449 = inter;
                                var30.arguments = inter.anObjectArray269;
                                PacketParser.aLinkedList_82.method1215(var30);
                            }

                            CS2Script var22;
                            if (inter.anObjectArray161 != null && PacketParser.anInt87 > inter.anInt284) {
                                if (inter.anIntArray211 != null && PacketParser.anInt87 - inter.anInt284 <= 32) {
                                    label531:
                                    for (var19 = inter.anInt284; var19 < PacketParser.anInt87; ++var19) {
                                        var29 = NPC.anIntArray3986[var19 & 31];

                                        for (var21 = 0; var21 < inter.anIntArray211.length; ++var21) {
                                            if (inter.anIntArray211[var21] == var29) {
                                                var22 = new CS2Script();
                                                var22.aClass11_2449 = inter;
                                                var22.arguments = inter.anObjectArray161;
                                                aLinkedList_1471.method1215(var22);
                                                break label531;
                                            }
                                        }
                                    }
                                } else {
                                    var30 = new CS2Script();
                                    var30.aClass11_2449 = inter;
                                    var30.arguments = inter.anObjectArray161;
                                    aLinkedList_1471.method1215(var30);
                                }

                                inter.anInt284 = PacketParser.anInt87;
                            }

                            if (inter.anObjectArray221 != null && anInt2317 > inter.anInt242) {
                                if (inter.anIntArray185 != null && anInt2317 - inter.anInt242 <= 32) {
                                    label512:
                                    for (var19 = inter.anInt242; var19 < anInt2317; ++var19) {
                                        var29 = Class163_Sub2_Sub1.anIntArray4025[var19 & 31];

                                        for (var21 = 0; var21 < inter.anIntArray185.length; ++var21) {
                                            if (inter.anIntArray185[var21] == var29) {
                                                var22 = new CS2Script();
                                                var22.aClass11_2449 = inter;
                                                var22.arguments = inter.anObjectArray221;
                                                aLinkedList_1471.method1215(var22);
                                                break label512;
                                            }
                                        }
                                    }
                                } else {
                                    var30 = new CS2Script();
                                    var30.aClass11_2449 = inter;
                                    var30.arguments = inter.anObjectArray221;
                                    aLinkedList_1471.method1215(var30);
                                }

                                inter.anInt242 = anInt2317;
                            }

                            if (inter.anObjectArray282 != null && Class36.anInt641 > inter.anInt213) {
                                if (inter.anIntArray286 != null && Class36.anInt641 - inter.anInt213 <= 32) {
                                    label493:
                                    for (var19 = inter.anInt213; var19 < Class36.anInt641; ++var19) {
                                        var29 = Class44.anIntArray726[var19 & 31];

                                        for (var21 = 0; var21 < inter.anIntArray286.length; ++var21) {
                                            if (inter.anIntArray286[var21] == var29) {
                                                var22 = new CS2Script();
                                                var22.aClass11_2449 = inter;
                                                var22.arguments = inter.anObjectArray282;
                                                aLinkedList_1471.method1215(var22);
                                                break label493;
                                            }
                                        }
                                    }
                                } else {
                                    var30 = new CS2Script();
                                    var30.aClass11_2449 = inter;
                                    var30.arguments = inter.anObjectArray282;
                                    aLinkedList_1471.method1215(var30);
                                }

                                inter.anInt213 = Class36.anInt641;
                            }

                            if (inter.anObjectArray174 != null && Unsorted.anInt944 > inter.anInt255) {
                                if (inter.anIntArray175 != null && Unsorted.anInt944 - inter.anInt255 <= 32) {
                                    label474:
                                    for (var19 = inter.anInt255; var19 < Unsorted.anInt944; ++var19) {
                                        var29 = QuickChatDefinition.anIntArray3565[var19 & 31];

                                        for (var21 = 0; var21 < inter.anIntArray175.length; ++var21) {
                                            if (inter.anIntArray175[var21] == var29) {
                                                var22 = new CS2Script();
                                                var22.aClass11_2449 = inter;
                                                var22.arguments = inter.anObjectArray174;
                                                aLinkedList_1471.method1215(var22);
                                                break label474;
                                            }
                                        }
                                    }
                                } else {
                                    var30 = new CS2Script();
                                    var30.aClass11_2449 = inter;
                                    var30.arguments = inter.anObjectArray174;
                                    aLinkedList_1471.method1215(var30);
                                }

                                inter.anInt255 = Unsorted.anInt944;
                            }

                            if (inter.anObjectArray158 != null && Class49.anInt815 > inter.anInt311) {
                                if (inter.anIntArray274 != null && Class49.anInt815 - inter.anInt311 <= 32) {
                                    label455:
                                    for (var19 = inter.anInt311; var19 < Class49.anInt815; ++var19) {
                                        var29 = anIntArray3780[var19 & 31];

                                        for (var21 = 0; var21 < inter.anIntArray274.length; ++var21) {
                                            if (inter.anIntArray274[var21] == var29) {
                                                var22 = new CS2Script();
                                                var22.aClass11_2449 = inter;
                                                var22.arguments = inter.anObjectArray158;
                                                aLinkedList_1471.method1215(var22);
                                                break label455;
                                            }
                                        }
                                    }
                                } else {
                                    var30 = new CS2Script();
                                    var30.aClass11_2449 = inter;
                                    var30.arguments = inter.anObjectArray158;
                                    aLinkedList_1471.method1215(var30);
                                }

                                inter.anInt311 = Class49.anInt815;
                            }

                            if (Class24.anInt472 > inter.anInt234 && inter.anObjectArray256 != null) {
                                var30 = new CS2Script();
                                var30.aClass11_2449 = inter;
                                var30.arguments = inter.anObjectArray256;
                                aLinkedList_1471.method1215(var30);
                            }

                            if (Class110.anInt1472 > inter.anInt234 && inter.anObjectArray156 != null) {
                                var30 = new CS2Script();
                                var30.aClass11_2449 = inter;
                                var30.arguments = inter.anObjectArray156;
                                aLinkedList_1471.method1215(var30);
                            }

                            if (Class167.anInt2087 > inter.anInt234 && inter.anObjectArray313 != null) {
                                var30 = new CS2Script();
                                var30.aClass11_2449 = inter;
                                var30.arguments = inter.anObjectArray313;
                                aLinkedList_1471.method1215(var30);
                            }

                            if (Class121.anInt1642 > inter.anInt234 && inter.anObjectArray268 != null) {
                                var30 = new CS2Script();
                                var30.aClass11_2449 = inter;
                                var30.arguments = inter.anObjectArray268;
                                aLinkedList_1471.method1215(var30);
                            }

                            if (Class140_Sub6.anInt2905 > inter.anInt234 && inter.anObjectArray315 != null) {
                                var30 = new CS2Script();
                                var30.aClass11_2449 = inter;
                                var30.arguments = inter.anObjectArray315;
                                aLinkedList_1471.method1215(var30);
                            }

                            inter.anInt234 = PacketParser.anInt3213;
                            if (inter.anObjectArray220 != null) {
                                for (var19 = 0; var19 < Class3_Sub23.anInt2537; ++var19) {
                                    CS2Script var31 = new CS2Script();
                                    var31.aClass11_2449 = inter;
                                    var31.inputTextCode = Class133.inputTextCodeArray[var19];
                                    var31.anInt2443 = Class120.anIntArray1638[var19];
                                    var31.arguments = inter.anObjectArray220;
                                    aLinkedList_1471.method1215(var31);
                                }
                            }

                            if (Class3_Sub28_Sub1.aBoolean3531 && inter.anObjectArray217 != null) {
                                var30 = new CS2Script();
                                var30.aClass11_2449 = inter;
                                var30.arguments = inter.anObjectArray217;
                                aLinkedList_1471.method1215(var30);
                            }
                        }
                    }

                    if (!inter.usingScripts && Class56.aClass11_886 == null && Class67.aClass11_1017 == null && !Class38_Sub1.aBoolean2615) {
                        if ((inter.anInt212 >= 0 || inter.anInt228 != 0) && Class126.anInt1676 >= var12 && Unsorted.anInt1709 >= var13 && Class126.anInt1676 < var14 && Unsorted.anInt1709 < var15) {
                            if (inter.anInt212 >= 0) {
                                Class107.aClass11_1453 = interfaces[inter.anInt212];
                            } else {
                                Class107.aClass11_1453 = inter;
                            }
                        }

                        if (inter.type == 8 && Class126.anInt1676 >= var12 && Unsorted.anInt1709 >= var13 && Class126.anInt1676 < var14 && Unsorted.anInt1709 < var15) {
                            Class20.aClass11_439 = inter;
                        }

                        if (inter.anInt252 > inter.height) {
                            Class137.method1819(Unsorted.anInt1709, inter.height, inter, Class126.anInt1676, var10 + inter.width, var11, inter.anInt252);
                        }
                    }

                    if (inter.type == 0) {
                        handleItemSwitch(interfaces, inter.componentHash, var12, var13, var14, var15, var10 - inter.anInt247, var11 - inter.anInt208);
                        if (inter.aClass11Array262 != null) {
                            handleItemSwitch(inter.aClass11Array262, inter.componentHash, var12, var13, var14, var15, var10 - inter.anInt247, var11 - inter.anInt208);
                        }

                        Class3_Sub31 var28 = TextureOperation23.aHashTable_3208.get(inter.componentHash);
                        if (var28 != null) {
                            GraphicDefinition.method967(var10, var13, var11, var14, var28.anInt2602, var12, var15);
                        }
                    }
                }
            }
        }

    }

    static boolean method51(RSInterface var0) {
        if (ClientCommands.commandQaOpEnabled) {
            if (method44(var0).anInt2205 != 0) {
                return false;
            }

            if (var0.type == 0) {
                return false;
            }
        }

        return var0.hidden;
    }

    final void method38() {
        try {
            if (Class143.gameStage != 1000) {
                boolean var2 = NPC.method1988();
                if (var2 && AudioHandler.musicEffectPlaying && WorldListEntry.aAudioChannel_2627 != null) {
                    WorldListEntry.aAudioChannel_2627.method2158();
                }

                if ((Class143.gameStage == 30 || Class143.gameStage == 10) && (Class3_Sub28_Sub5.forceReplaceCanvasEnable || Class53.aLong866 != 0 && Class53.aLong866 < TimeUtils.time())) {
                    GameObject.graphicsSettings(Class3_Sub28_Sub5.forceReplaceCanvasEnable, Class83.getWindowType(), TextureOperation.anInt2378, Unsorted.anInt3071);
                }

                int var4;
                int var5;
                if (null == TextureOperation30.fullScreenFrame) {
                    Object var3;
                    if (GameShell.frame == null) {
                        var3 = Class38.gameSignlink.gameApplet;
                    } else {
                        var3 = GameShell.frame;
                    }

                    var4 = ((Container) var3).getSize().width;
                    var5 = ((Container) var3).getSize().height;
                    if (var3 == GameShell.frame) {
                        Insets var6 = GameShell.frame.getInsets();
                        var4 -= var6.right + var6.left;
                        var5 -= var6.top + var6.bottom;
                    }

                    if (var4 != Unsorted.frameWidth || Class70.frameHeight != var5) {
                        if (Signlink.osName.startsWith("mac")) {
                            Unsorted.frameWidth = var4;
                            Class70.frameHeight = var5;
                        } else {
                            Class119.method1729();
                        }

                        Class53.aLong866 = TimeUtils.time() - -500L;
                    }
                }

                if (TextureOperation30.fullScreenFrame != null && !TextureOperation26.aBoolean3078 && (30 == Class143.gameStage || 10 == Class143.gameStage)) {
                    GameObject.graphicsSettings(false, Unsorted.anInt2577, -1, -1);
                }

                boolean var10 = false;
                if (TextureOperation30.fullRedraw) {
                    var10 = true;
                    TextureOperation30.fullRedraw = false;
                }

                if (var10) {
                    Unsorted.method1396(40 ^ -41);
                }

                if (HDToolKit.highDetail) {
                    for (var4 = 0; var4 < 100; ++var4) {
                        Unsorted.aBooleanArray3674[var4] = true;
                    }
                }
                if (Class143.gameStage == 0) {
                    if (Discord.checkInitializable()) {
                        Discord.initialize();
                        Discord.updatePresence("At the login screen", "", "");
                    }
                    StartupLoadingBarInitial.draw(ColorCore.loadingbarcolor, var10, loadingBarTextToDisplay, LoadingStageNumber);
                } else if (5 == Class143.gameStage) {
                    StartupLoadingBar.draw(false, FontType.bold);
                } else if (Class143.gameStage == 10) {
                    Class3_Sub17.method381();
                } else if (25 != Class143.gameStage && Class143.gameStage != 28) {
                    if (Class143.gameStage == 30) {
                        Class49.method1127(0);
                    } else if (40 == Class143.gameStage) {
                        LoadingBox.draw(false, RSString.stringCombiner(new RSString[]{TextCore.ConxLost, TextCore.aClass94_2598, TextCore.AttemptingReestablish}));
                    }
                } else if (Class163_Sub2_Sub1.anInt4019 == 1) {
                    if (Class40.anInt3293 > LinkableRSString.anInt2579) {
                        LinkableRSString.anInt2579 = Class40.anInt3293;
                    }

                    var4 = 50 * (LinkableRSString.anInt2579 + -Class40.anInt3293) / LinkableRSString.anInt2579;
                    LoadingBox.draw(false, RSString.stringCombiner(new RSString[]{TextCore.LoadingPleaseWait2, TextCore.aClass94_3399, RSString.stringAnimator(var4), RSString.parse("(U(Y")}));
                } else if (Class163_Sub2_Sub1.anInt4019 == 2) {
                    if (anInt2275 < Class162.anInt2038) {
                        anInt2275 = Class162.anInt2038;
                    }

                    var4 = (-Class162.anInt2038 + anInt2275) * 50 / anInt2275 + 50;
                    LoadingBox.draw(false, RSString.stringCombiner(new RSString[]{TextCore.LoadingPleaseWait2, TextCore.aClass94_3399, RSString.stringAnimator(var4), RSString.parse("(U(Y")}));
                } else {
                    LoadingBox.draw(false, TextCore.LoadingPleaseWait2);
                }

                DeveloperConsole.INSTANCE.preDraw();
                DeveloperConsole.INSTANCE.draw();

                if (HDToolKit.highDetail && Class143.gameStage != 0) {
                    HDToolKit.bufferSwap();

                    for (var4 = 0; Class3_Sub28_Sub3.anInt3557 > var4; ++var4) {
                        Class163_Sub1_Sub1.aBooleanArray4008[var4] = false;
                    }
                } else {
                    Graphics var11;
                    if ((Class143.gameStage == 30 || 10 == Class143.gameStage) && rectDebugInt == 0 && !var10) {
                        try {
                            var11 = GameShell.canvas.getGraphics();

                            for (var5 = 0; Class3_Sub28_Sub3.anInt3557 > var5; ++var5) {
                                if (Class163_Sub1_Sub1.aBooleanArray4008[var5]) {
                                    Unsorted.aClass158_3009.drawGraphics(Class3_Sub28_Sub18.anIntArray3768[var5], AudioChannel.anIntArray1969[var5], Class140_Sub4.anIntArray2794[var5], var11, Player.anIntArray3954[var5]);
                                    Class163_Sub1_Sub1.aBooleanArray4008[var5] = false;
                                }
                            }
                        } catch (Exception var8) {
                            GameShell.canvas.repaint();
                        }
                    } else if (0 != Class143.gameStage) {
                        try {
                            var11 = GameShell.canvas.getGraphics();
                            Unsorted.aClass158_3009.method2179(var11);

                            for (var5 = 0; var5 < Class3_Sub28_Sub3.anInt3557; ++var5) {
                                Class163_Sub1_Sub1.aBooleanArray4008[var5] = false;
                            }
                        } catch (Exception var7) {
                            var7.printStackTrace();
                            GameShell.canvas.repaint();
                        }
                    }
                }

                if (sweepReferenceCache) {
                    Class75_Sub3.sweepClientStartupReferenceCache();
                }

                if (Unsorted.aBoolean2146 && 10 == Class143.gameStage && ConfigInventoryDefinition.anInt3655 != -1) {
                    Unsorted.aBoolean2146 = false;
                    Class119.method1730(Class38.gameSignlink);
                }

                DeveloperConsole.INSTANCE.postDraw();
				if (DeveloperConsole.INSTANCE.getOpen()) {
				    DeveloperConsole.INSTANCE.draw();
//					System.out.println("Draw developer console");
				}
                DiscordRPC.discordRunCallbacks();
            }
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "client.K(" + 40 + ')');
        }
    }

    final void method32() {
        try {
            if (HDToolKit.highDetail) {
                HDToolKit.method1842();
            }

            if (null != TextureOperation30.fullScreenFrame) {
                Unsorted.method593(TextureOperation30.fullScreenFrame, Class38.gameSignlink);
                TextureOperation30.fullScreenFrame = null;
            }

            if (null != Class38.gameSignlink) {
                Class38.gameSignlink.method1442(this.getClass(), 0);
            }

            if (null != TextureOperation20.aClass67_1443) {
                TextureOperation20.aClass67_1443.aBoolean1015 = false;
            }

            TextureOperation20.aClass67_1443 = null;
            if (Class3_Sub15.activeConnection != null) {
                Class3_Sub15.activeConnection.close();
                Class3_Sub15.activeConnection = null;
            }

            Class163_Sub1_Sub1.method2215(GameShell.canvas);
            Unsorted.method1783(GameShell.canvas);
            if (null != Class38.aClass146_668) {
                Class38.aClass146_668.method2082(GameShell.canvas);
            }

            TextureOperation12.method167(0);
            MouseListeningClass.method2090();
            Class38.aClass146_668 = null;
            if (null != WorldListEntry.aAudioChannel_2627) {
                WorldListEntry.aAudioChannel_2627.method2163();
            }

            if (null != Class3_Sub21.aAudioChannel_2491) {
                Class3_Sub21.aAudioChannel_2491.method2163();
            }

            Class58.aJs5Worker_917.close();
            TextureOperation31.aCacheResourceWorker_3159.stop();

            try {
                if (Class101.aClass30_1422 != null) {
                    Class101.aClass30_1422.method980();
                }

                if (Class163_Sub2.aClass30Array2998 != null) {
                    for (int var2 = 0; var2 < Class163_Sub2.aClass30Array2998.length; ++var2) {
                        if (null != Class163_Sub2.aClass30Array2998[var2]) {
                            Class163_Sub2.aClass30Array2998[var2].method980();
                        }
                    }
                }

                if (null != aClass30_1572) {
                    aClass30_1572.method980();
                }

                if (null != Unsorted.aClass30_1039) {
                    Unsorted.aClass30_1039.method980();
                }
            } catch (IOException var3) {
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "client.F(" + (byte) 23 + ')');
        }
    }

    public final void init() {
        try {
            GameConfig.WORLD = GameConfig.WORLD_OVERRIDE == -1 ? ObjectDefinition.paramWorldID : GameConfig.WORLD_OVERRIDE;
            ObjectDefinition.paramWorldID = GameConfig.WORLD_OVERRIDE == -1 ? 1 : GameConfig.WORLD_OVERRIDE;

            Class44.paramModeWhere = Integer.parseInt(Objects.requireNonNull(this.getParameter("modewhere")));
            if (Class44.paramModeWhere < LIVE_ENVIRONMENT || Class44.paramModeWhere > LOCAL_ENVIRONMENT) {
                Class44.paramModeWhere = 0;
            }

            TextureOperation20.paramModeWhat = Integer.parseInt(Objects.requireNonNull(this.getParameter("modewhat")));
            if (TextureOperation20.paramModeWhat < 0 || TextureOperation20.paramModeWhat > 2) {
                TextureOperation20.paramModeWhat = 0;
            }

            String var1 = this.getParameter("advertsuppressed");
            paramAdvertisementSuppressed = var1 != null && var1.equals("1");

            try {
                Class3_Sub20.paramLanguage = Integer.parseInt(Objects.requireNonNull(this.getParameter("lang")));
            } catch (Exception var10) {
                Class3_Sub20.paramLanguage = 0;
            }
            Unsorted.languageSetter(Class3_Sub20.paramLanguage);

            String var2 = this.getParameter("objecttag");
            Class163_Sub2_Sub1.paramObjectTagEnabled = var2 != null && var2.equals("1");

            String var3 = this.getParameter("js");
            Unsorted.paramJavaScriptEnabled = null != var3 && var3.equals("1");

            String var4 = this.getParameter("game");
            if (var4 != null && var4.equals("1")) {
                Class158.paramGameTypeID = 1;
            } else {
                Class158.paramGameTypeID = 0;
            }

            try {
                Class3_Sub26.paramAffid = Integer.parseInt(Objects.requireNonNull(this.getParameter("affid")));
            } catch (Exception var9) {
                Class3_Sub26.paramAffid = 0;
            }

            Class163_Sub2.paramSettings = TextCore.aClass94_1745.getParamValue(this);
            if (Class163_Sub2.paramSettings == null) {
                Class163_Sub2.paramSettings = RSString.parse("");
            }

            String var5 = this.getParameter("country");
            if (var5 != null) {
                try {
                    Class3_Sub31.paramCountryID = Integer.parseInt(var5);
                } catch (Exception var8) {
                    Class3_Sub31.paramCountryID = 0;
                }
            }

            String var6 = this.getParameter("haveie6");
            Class106.paramUserUsingInternetExplorer = null != var6 && var6.equals("1");

            clientInstance = this;
            this.method41(32 - -TextureOperation20.paramModeWhat);
        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "client.init()");
        }
    }

    final void method39() {
        try {
            Class119.method1729();
            TextureOperation31.aCacheResourceWorker_3159 = new CacheResourceWorker();
            Class58.aJs5Worker_917 = new Js5Worker();

            if (TextureOperation20.paramModeWhat != LIVE_ENVIRONMENT) {
                Class3_Sub6.softReferenceTestArray = new byte[50][];
            }

            CS2Script.userCurrentWorldID = ObjectDefinition.paramWorldID;

            Unsorted.parsePreferences(Class38.gameSignlink);
            SystemLogger.logInfo("port: " + Class53.anInt867);
            SystemLogger.logInfo("MSIP: " + GameConfig.IP_MANAGEMENT);

            if (Class44.paramModeWhere == LIVE_ENVIRONMENT) {
                ClientErrorException.worldListHost = Objects.requireNonNull(this.getCodeBase()).getHost();
                Class53.anInt867 = GameConfig.SERVER_PORT + ObjectDefinition.paramWorldID; //443 is secure port
                anInt3773 = '\uaa4a';

            } else if (Class44.paramModeWhere == OFFICE_ENVIRONMENT) {
                ClientErrorException.worldListHost = Objects.requireNonNull(this.getCodeBase()).getHost();
                //System.out.println("port = " + Class53.anInt867);
                Class53.anInt867 = ObjectDefinition.paramWorldID + 50000;
                anInt3773 = 40000 + ObjectDefinition.paramWorldID;

            } else if (Class44.paramModeWhere == LOCAL_ENVIRONMENT) {
                ClientErrorException.worldListHost = "127.0.0.1";
                System.out.println("Setting worldListHost to 127.0.0.1, port = " + Class53.anInt867);
                Class53.anInt867 = ObjectDefinition.paramWorldID + 50000;
                anInt3773 = ObjectDefinition.paramWorldID + 40000;
            }

            switch (Class158.paramGameTypeID) {
                case GAME_TYPE_RUNESCAPE:
                    AudioThread.aShortArrayArray344 = RunescapeGameAppearance.INSTANCE.getAShortArrayArray3654();
                    Class91.aShortArray1311 = RunescapeGameAppearance.INSTANCE.getAShortArray3349();
                    Class101.aShortArrayArray1429 = RunescapeGameAppearance.INSTANCE.getAShortArrayArray435();
                    Class3_Sub25.aShortArray2548 = RunescapeGameAppearance.INSTANCE.getAShortArray3011();
                    break;

                case GAME_TYPE_MECHSCAPE:
                    ClientCommands.shiftClickEnabled = true;
                    Class92.defaultScreenColorRgb = 16777215;
                    Class92.defaultRegionAmbientRGB = 0;
                    AudioThread.aShortArrayArray344 = MechscapeGameAppearance.INSTANCE.getAShortArrayArray1619();
                    Class101.aShortArrayArray1429 = MechscapeGameAppearance.INSTANCE.getAShortArrayArray2634();
                    Class3_Sub25.aShortArray2548 = MechscapeGameAppearance.INSTANCE.getAShortArray63();
                    Class91.aShortArray1311 = MechscapeGameAppearance.INSTANCE.getAShortArray2219();
                    break;

            }

            currentPort = Class53.anInt867;
            Class162.anInt2036 = anInt3773;
            Class38_Sub1.accRegistryIp = GameConfig.IP_MANAGEMENT;
            Class123.anInt1658 = anInt3773;
            TextureOperation38.aShortArray3455 = TextureOperation16.aShortArray3110 = Class136.aShortArray1779 = TextureOperation38.aShortArray3453 = new short[256];

            Class140_Sub6.accRegistryPort = Class123.anInt1658;
            if (Signlink.anInt1214 == 3 && 2 != Class44.paramModeWhere) {
                CS2Script.userCurrentWorldID = ObjectDefinition.paramWorldID;
            }

            KeyboardListener.adjustKeyCodeMap();
            TextureOperation34.method193((byte) 115, GameShell.canvas);
            ItemDefinition.method1119(GameShell.canvas, false);
            Class38.aClass146_668 = Class21.method916();
            if (null != Class38.aClass146_668) {
                Class38.aClass146_668.method2084(GameShell.canvas, -97);
            }

            try {
                if (Class38.gameSignlink.cacheDataFile != null) {
                    Class101.aClass30_1422 = new Class30(Class38.gameSignlink.cacheDataFile, 5200);

                    for (int var2 = 0; var2 < 29; ++var2) {
                        Class163_Sub2.aClass30Array2998[var2] = new Class30(Class38.gameSignlink.cacheIndicesFiles[var2], 6000);
                    }

                    aClass30_1572 = new Class30(Class38.gameSignlink.cacheChecksumFile, 6000);
                    AtmosphereParser.aClass41_1186 = new Class41(255, Class101.aClass30_1422, aClass30_1572, 500000);
                    Unsorted.aClass30_1039 = new Class30(Class38.gameSignlink.randomDatFile, 24);
                    Class38.gameSignlink.cacheIndicesFiles = null;
                    Class38.gameSignlink.cacheChecksumFile = null;
                    Class38.gameSignlink.randomDatFile = null;
                    Class38.gameSignlink.cacheDataFile = null;
                }
            } catch (IOException var3) {
                Unsorted.aClass30_1039 = null;
                Class101.aClass30_1422 = null;
                aClass30_1572 = null;
                AtmosphereParser.aClass41_1186 = null;
            }

            Class167.aClass94_2083 = TextCore.RSLoadingPleaseWait;
            if (Class44.paramModeWhere != 0) {
                ClientCommands.fpsOverlayEnabled = true;
            }
            //Class3_Sub26.tweeningEnabled  = true;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "client.B(" + 2 + ')');
        }
    }

    final void method33() {
        // TODO Do we still need this method here?
    }

    private void method46(int var2) {
        try {
            ++Class58.aJs5Worker_917.errors;
            Class17.aClass64_413 = null;

            Class58.aJs5Worker_917.status = var2;
            Unsorted.js5Connection = null;
            PacketParser.anInt80 = 0;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "client.P(" + true + ',' + var2 + ')');
        }
    }

    private void method47() {
        try {
            for (Class3_Sub23.anInt2537 = 0; Unsorted.method591(83) && Class3_Sub23.anInt2537 < 128; ++Class3_Sub23.anInt2537) {
                Class133.inputTextCodeArray[Class3_Sub23.anInt2537] = Class3_Sub28_Sub9.anInt3624;
                Class120.anIntArray1638[Class3_Sub23.anInt2537] = TextureOperation7.anInt3342;
            }

            ++Class106.anInt1446;
            if (-1 != ConfigInventoryDefinition.anInt3655) {
                GraphicDefinition.method967(0, 0, 0, Class23.canvasWidth, ConfigInventoryDefinition.anInt3655, 0, Class140_Sub7.canvasHeight);
            }

            ++PacketParser.anInt3213;
            if (HDToolKit.highDetail) {
                int var2 = 19137023;

                label191:
                for (int var3 = 0; var3 < 32768; ++var3) {
                    NPC var4 = NPC.npcs[var3];
                    if (null != var4) {
                        byte var5 = var4.definition.aByte1267;
                        if ((var5 & 2) > 0 && var4.anInt2816 == 0 && 10.0D > Math.random() * 1000.0D) {
                            int var6 = (int) Math.round(-1.0D + 2.0D * Math.random());
                            int var7 = (int) Math.round(Math.random() * 2.0D - 1.0D);
                            if (var6 != 0 || 0 != var7) {
                                var4.aByteArray2795[0] = 1;
                                var4.anIntArray2767[0] = var6 + (var4.xAxis >> 7);
                                var4.anIntArray2755[0] = var7 + (var4.zAxis >> 7);
                                AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].method1502(var4.xAxis >> 7, var4.getSize(), false, 0, var4.getSize(), var4.zAxis >> 7);
                                if (0 <= var4.anIntArray2767[0] && var4.anIntArray2767[0] <= 104 + -var4.getSize() && 0 <= var4.anIntArray2755[0] && var4.anIntArray2755[0] <= 104 - var4.getSize() && AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].method1500(var4.zAxis >> 7, var4.anIntArray2755[0], var4.anIntArray2767[0], var4.xAxis >> 7)) {
                                    if (var4.getSize() > 1) {
                                        for (int var8 = var4.anIntArray2767[0]; var8 < var4.anIntArray2767[0] - -var4.getSize(); ++var8) {
                                            for (int var9 = var4.anIntArray2755[0]; var4.anIntArray2755[0] + var4.getSize() > var9; ++var9) {
                                                if ((var2 & AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].anIntArrayArray1304[var8][var9]) != 0) {
                                                    continue label191;
                                                }
                                            }
                                        }
                                    }

                                    var4.anInt2816 = 1;
                                }
                            }
                        }

                        Unsorted.method1180((byte) -122, var4);
                        Class17.method904(65536, var4);
                        RenderAnimationDefinition.method900(var4);
                        AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].method1489(var4.xAxis >> 7, false, (byte) 85, var4.zAxis >> 7, var4.getSize(), var4.getSize());
                    }
                }
            }

            if (!HDToolKit.highDetail) {
                Unsorted.method744();
            } else if (0 == LoginHandler.loginStage && 0 == Unsorted.registryStage) {
                if (Class133.anInt1753 == 2) {
                    CS2Script.method379();
                } else {
                    InterfaceWidget.d(65535);
                }

                if (14 > NPC.anInt3995 >> 7 || NPC.anInt3995 >> 7 >= 90 || 14 > Class77.anInt1111 >> 7 || Class77.anInt1111 >> 7 >= 90) {
                    TextureOperation26.method195();
                }
            }

            while (true) {
                CS2Script var11 = (CS2Script) PacketParser.aLinkedList_82.method1220();
                RSInterface var12;
                RSInterface var13;
                if (var11 == null) {
                    while (true) {
                        var11 = (CS2Script) Class65.aLinkedList_983.method1220();
                        if (null == var11) {
                            while (true) {
                                var11 = (CS2Script) aLinkedList_1471.method1220();
                                if (null == var11) {
                                    if (Class56.aClass11_886 != null) {
                                        PacketParser.method829();
                                    }

                                    if (null != AudioThread.aClass64_351 && AudioThread.aClass64_351.anInt978 == 1) {
                                        if (null != AudioThread.aClass64_351.anObject974) {
                                            Class99.method1596(TextureOperation5.aClass94_3295, (byte) 126, Unsorted.aBoolean2154);
                                        }

                                        Unsorted.aBoolean2154 = false;
                                        TextureOperation5.aClass94_3295 = null;
                                        AudioThread.aClass64_351 = null;
                                    }

                                    if (Class44.anInt719 % 1500 == 0) {
                                        Class72.method1293();
                                    }

                                    return;
                                }

                                var12 = var11.aClass11_2449;
                                if (0 <= var12.anInt191) {
                                    var13 = Unsorted.getRSInterface(var12.parentId);
                                    if (var13 == null || null == var13.aClass11Array262 || var12.anInt191 >= var13.aClass11Array262.length || var12 != var13.aClass11Array262[var12.anInt191]) {
                                        continue;
                                    }
                                }

                                Class43.method1065(var11);
                            }
                        }

                        var12 = var11.aClass11_2449;
                        if (var12.anInt191 >= 0) {
                            var13 = Unsorted.getRSInterface(var12.parentId);
                            if (null == var13 || var13.aClass11Array262 == null || var13.aClass11Array262.length <= var12.anInt191 || var12 != var13.aClass11Array262[var12.anInt191]) {
                                continue;
                            }
                        }

                        Class43.method1065(var11);
                    }
                }

                var12 = var11.aClass11_2449;
                if (var12.anInt191 >= 0) {
                    var13 = Unsorted.getRSInterface(var12.parentId);
                    if (null == var13 || null == var13.aClass11Array262 || var12.anInt191 >= var13.aClass11Array262.length || var12 != var13.aClass11Array262[var12.anInt191]) {
                        continue;
                    }
                }

                Class43.method1065(var11);
            }
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "client." + (byte) 1 + ')');
        }
    }

    private void method48() {
        try {
            boolean var2 = Class58.aJs5Worker_917.process();
            if (!var2) {
                this.method49();
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "client.J(" + true + ')');
        }
    }

    private void method49() {
        if (Class58.aJs5Worker_917.errors > Class163_Sub2_Sub1.anInt4026) {
            anInt3068 = 5 * 50 * (Class58.aJs5Worker_917.errors + -1);

            //Port swap
            if (Class162.anInt2036 == Class140_Sub6.accRegistryPort) {
                Class140_Sub6.accRegistryPort = currentPort;
            } else {
                Class140_Sub6.accRegistryPort = Class162.anInt2036;
            }

            if (anInt3068 > 3000) {
                anInt3068 = 3000;
            }

            if (Class58.aJs5Worker_917.errors >= 2 && Class58.aJs5Worker_917.status == 6) {
                this.errorPrint("js5connect_outofdate");
                Class143.gameStage = 1000;
                return;
            }

            if (Class58.aJs5Worker_917.errors >= 4 && Class58.aJs5Worker_917.status == -1) {
                this.errorPrint("js5crc");
                Class143.gameStage = 1000;
                return;
            }

            if (Class58.aJs5Worker_917.errors >= 4 && (Class143.gameStage == 0 || Class143.gameStage == 5)) {
                if (Class58.aJs5Worker_917.status == 7 || Class58.aJs5Worker_917.status == 9) {
                    this.errorPrint("js5connect_full");
                } else if (Class58.aJs5Worker_917.status > 0) {
                    this.errorPrint("js5connect");
                } else {
                    this.errorPrint("js5io");
                }

                Class143.gameStage = 1000;
                return;
            }
        }

        Class163_Sub2_Sub1.anInt4026 = Class58.aJs5Worker_917.errors;
        if (anInt3068 > 0) {
            --anInt3068;
        } else {
            try {
                if (PacketParser.anInt80 == 0) {
                    System.out.println("Trying " + GameConfig.Companion.getJS5_SERVER_PORT());
                    Class17.aClass64_413 = Class38.gameSignlink.method1441((byte) 8, Class38_Sub1.accRegistryIp, GameConfig.Companion.getJS5_SERVER_PORT());
                    ++PacketParser.anInt80;
                }

                if (PacketParser.anInt80 == 1) {

                    /* If the connection is null we reset the JS5 port to the backup server JS5 for compatibility reasons */
                    if (2 == Objects.requireNonNull(Class17.aClass64_413).anInt978) {
                        GameConfig.Companion.setJS5_SERVER_PORT(GameConfig.SERVER_PORT + ObjectDefinition.paramWorldID);
                        this.method46(1000);
                        return;
                    }

                    if (Class17.aClass64_413.anInt978 == 1) {
                        ++PacketParser.anInt80;
                    }
                }

                if (2 == PacketParser.anInt80) {
                    Unsorted.js5Connection = new Connection((Socket) Objects.requireNonNull(Class17.aClass64_413).anObject974, Class38.gameSignlink);
                    DataBuffer var2 = new DataBuffer(9);
                    var2.writeByte(15); //JS5 handshake
                    var2.writeInt(GameConfig.CLIENT_BUILD);
                    var2.writeInt(GameConfig.CLIENT_VERSION);
                    Unsorted.js5Connection.sendBytes(var2.buffer, 9);
                    ++PacketParser.anInt80;
                    TextureOperation13.aLong3366 = TimeUtils.time();
                }

                if (3 == PacketParser.anInt80) {
                    if (Class143.gameStage != 0 && Class143.gameStage != 5 && 0 >= Unsorted.js5Connection.availableBytes()) {
                        if (TimeUtils.time() + -TextureOperation13.aLong3366 > 30000) {
                            this.method46(1001);
                            return;
                        }
                    } else {
                        int var5 = Unsorted.js5Connection.readByte();
                        if (var5 != 0) {
                            this.method46(var5);
                            return;
                        }

                        ++PacketParser.anInt80;
                    }
                }

                if (PacketParser.anInt80 == 4) {
                    boolean var6 = Class143.gameStage == 5 || Class143.gameStage == 10 || Class143.gameStage == 28;
                    Class58.aJs5Worker_917.connect(!var6, Unsorted.js5Connection);
                    Unsorted.js5Connection = null;
                    Class17.aClass64_413 = null;
                    PacketParser.anInt80 = 0;
                }
            } catch (IOException var3) {
                this.method46(1002);
            }

        }
    }

    private void method52(int var1) {
        try {
            if (!Unsorted.aBoolean2146) {
                while (Unsorted.method591(107)) {
                    if (TextureOperation7.anInt3342 == 115 || TextureOperation7.anInt3342 == 83) {
                        Unsorted.aBoolean2146 = true;
                    }
                }
            }

            if (var1 >= 46) {
                int var3;
                if (anInt1354 == 0) {
                    Runtime var10 = Runtime.getRuntime();
                    var3 = (int) ((var10.totalMemory() - var10.freeMemory()) / 1024L);
                    long var4 = TimeUtils.time();
                    if (aLong3296 == 0) {
                        aLong3296 = var4;
                    }

                    if (var3 > 16384 && 5000L > -aLong3296 + var4) {
                        if (-aLong1310 + var4 > 1000L) {
                            System.gc();
                            aLong1310 = var4;
                        }

                        LoadingStageNumber = 5;
                        loadingBarTextToDisplay = TextCore.AllocatingMemory;
                    } else {
                        loadingBarTextToDisplay = TextCore.AllocatedMemory;
                        anInt1354 = 10;
                        LoadingStageNumber = 5;
                    }
                } else {
                    int var2;
                    if (anInt1354 == 10) {
                        Class68.method1267();

                        for (var2 = 0; var2 < 4; ++var2) {
                            AtmosphereParser.aClass91Array1182[var2] = new Class91();
                        }

                        LoadingStageNumber = 10;
                        anInt1354 = 30;
                        loadingBarTextToDisplay = TextCore.CreatedWorld;
                    } else if (anInt1354 == 30) {
                        if (Unsorted.aClass8_1936 == null) {
                            Unsorted.aClass8_1936 = new Class8(Class58.aJs5Worker_917, TextureOperation31.aCacheResourceWorker_3159);
                        }

                        if (Unsorted.aClass8_1936.method837()) {
                            CacheIndex.skeletonsIndex = Class8.getCacheIndex(false, true, true, 0);
                            CacheIndex.skinsIndex = Class8.getCacheIndex(false, true, true, 1);
                            CacheIndex.configurationsIndex = Class8.getCacheIndex(true, true, false, 2);
                            CacheIndex.interfacesIndex = Class8.getCacheIndex(false, true, true, 3);
                            CacheIndex.soundFXIndex = Class8.getCacheIndex(false, true, true, 4);
                            CacheIndex.landscapesIndex = Class8.getCacheIndex(true, true, true, 5);
                            CacheIndex.musicIndex = Class8.getCacheIndex(true, false, true, 6);
                            CacheIndex.modelsIndex = Class8.getCacheIndex(false, true, true, 7);
                            CacheIndex.spritesIndex = Class8.getCacheIndex(false, true, true, 8);
                            CacheIndex.texturesIndex = Class8.getCacheIndex(false, true, true, 9);
                            CacheIndex.huffmanEncodingIndex = Class8.getCacheIndex(false, true, true, 10);
                            CacheIndex.music2Index = Class8.getCacheIndex(false, true, true, 11);
                            CacheIndex.interfaceScriptsIndex = Class8.getCacheIndex(false, true, true, 12);
                            CacheIndex.fontsIndex = Class8.getCacheIndex(false, true, true, 13);
                            CacheIndex.soundFX2Index = Class8.getCacheIndex(false, false, true, 14);
                            CacheIndex.soundFX3Index = Class8.getCacheIndex(false, true, true, 15);
                            CacheIndex.objectConfigIndex = Class8.getCacheIndex(false, true, true, 16);
                            CacheIndex.clientscriptMaskIndex = Class8.getCacheIndex(false, true, true, 17);
                            CacheIndex.npcConfigIndex = Class8.getCacheIndex(false, true, true, 18);
                            CacheIndex.itemConfigIndex = Class8.getCacheIndex(false, true, true, 19);
                            CacheIndex.animationIndex = Class8.getCacheIndex(false, true, true, 20);
                            CacheIndex.graphicFXIndex = Class8.getCacheIndex(false, true, true, 21);
                            CacheIndex.clientScriptConfigIndex = Class8.getCacheIndex(false, true, true, 22);
                            CacheIndex.worldmapIndex = Class8.getCacheIndex(true, true, true, 23);
                            CacheIndex.quickchatMessagesIndex = Class8.getCacheIndex(false, true, true, 24);
                            CacheIndex.quickchatMenusIndex = Class8.getCacheIndex(false, true, true, 25);
                            CacheIndex.materialsIndex = Class8.getCacheIndex(true, true, true, 26);
                            CacheIndex.particlesConfigIndex = Class8.getCacheIndex(false, true, true, 27);
                            CacheIndex.libIndex = Class8.getCacheIndex(false, true, true, 28);
                            LoadingStageNumber = 15;
                            loadingBarTextToDisplay = TextCore.ConxUpdateServer;
                            anInt1354 = 40;
                        } else {
                            loadingBarTextToDisplay = TextCore.LoadingConnecting;
                            LoadingStageNumber = 12;
                        }
                    } else if (anInt1354 == 40) {
                        var2 = 0;

                        for (var3 = 0; var3 < 29; ++var3) {
                            var2 += Unsorted.aClass151_Sub1Array2601[var3].method2111() * anIntArray3288[var3] / 100;
                        }

                        if (var2 < 100) { //!= 100
                            if (var2 != 0) {
                                loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.CheckingForUpdates, RSString.stringAnimator(var2), RSString.parse("(U")});
                            }

                            LoadingStageNumber = 20;
                        } else {
                            LoadingStageNumber = 20;
                            loadingBarTextToDisplay = TextCore.LoadedUpdateList;
                            Sprites.getSpriteFromArchive(CacheIndex.spritesIndex);
                            Class97.method1593(111, CacheIndex.spritesIndex);
                            TextureOperation20.method233(28280, CacheIndex.spritesIndex);
                            anInt1354 = 45;
                        }
                    } else if (anInt1354 == 45) {
                        Class140_Sub3.method1959(TextureOperation17.stereoSound);
                        aClass3_Sub24_Sub4_1193 = new Class3_Sub24_Sub4();
                        aClass3_Sub24_Sub4_1193.method479();
                        WorldListEntry.aAudioChannel_2627 = AudioChannel.method1195(22050, Class38.gameSignlink, GameShell.canvas, 0);
                        WorldListEntry.aAudioChannel_2627.method2154(aClass3_Sub24_Sub4_1193);
                        AudioHandler.method897(aClass3_Sub24_Sub4_1193, CacheIndex.soundFX3Index, CacheIndex.soundFX2Index, CacheIndex.soundFXIndex);
                        Class3_Sub21.aAudioChannel_2491 = AudioChannel.method1195(2048, Class38.gameSignlink, GameShell.canvas, 1);
                        Class3_Sub26.aClass3_Sub24_Sub2_2563 = new Class3_Sub24_Sub2();
                        Class3_Sub21.aAudioChannel_2491.method2154(Class3_Sub26.aClass3_Sub24_Sub2_2563);
                        Class27.resampler = new Class157(22050, Class21.sampleRate);

                        int loginThemeID;
                        if (GameConfig.EASTER_EVENT_ENABLED) {
                            loginThemeID = CacheIndex.musicIndex.getArchiveForName(RSString.parse("Funny Bunnies"));
                        } else {
                            loginThemeID = CacheIndex.musicIndex.getArchiveForName(RSString.parse(GameConfig.LOGIN_THEME));
                        }

                        KeyboardListener.loginThemeSongArchiveID = loginThemeID;
                        LoadingStageNumber = 30;
                        anInt1354 = 50;
                        loadingBarTextToDisplay = TextCore.PreparedSoundEngine;
                    } else if (anInt1354 == 50) {
                        var2 = Sprites.method228(CacheIndex.spritesIndex, CacheIndex.fontsIndex);
                        var3 = 6;
                        if (var3 <= var2) {
                            loadingBarTextToDisplay = TextCore.LoadedFonts;
                            LoadingStageNumber = 35;
                            anInt1354 = 60;
                        } else {
                            loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingFonts, RSString.stringAnimator(100 * var2 / var3), RSString.parse("(U")});
                            LoadingStageNumber = 35;
                        }
                    } else if (60 == anInt1354) {
                        var2 = Unsorted.method599(CacheIndex.spritesIndex);
                        var3 = 2;
                        if (var3 <= var2) {
                            loadingBarTextToDisplay = TextCore.LoadedTitleScreen;
                            anInt1354 = 65;
                        } else {
                            loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingTitleScreen, RSString.stringAnimator(100 * var2 / var3), RSString.parse("(U")});
                        }
                        LoadingStageNumber = 40;
                    } else if (anInt1354 == 65) {
                        Class3_Sub28_Sub9.method581(CacheIndex.fontsIndex, CacheIndex.spritesIndex);
                        LoadingStageNumber = 45;
                        loadingBarTextToDisplay = TextCore.OpenedTitleScreen;
                        Class117.method1719(5);
                        anInt1354 = 70;
                    } else if (anInt1354 == 70) {
                        CacheIndex.configurationsIndex.method2113();
                        byte var7 = 0;
                        var2 = var7 + CacheIndex.configurationsIndex.method2136((byte) -124);
                        CacheIndex.objectConfigIndex.method2113();
                        var2 += CacheIndex.objectConfigIndex.method2136((byte) -128);
                        CacheIndex.libIndex.method2113();
                        var2 += CacheIndex.libIndex.method2136((byte) -124);
                        CacheIndex.clientscriptMaskIndex.method2113();
                        var2 += CacheIndex.clientscriptMaskIndex.method2136((byte) -123);
                        CacheIndex.npcConfigIndex.method2113();
                        var2 += CacheIndex.npcConfigIndex.method2136((byte) -127);
                        CacheIndex.itemConfigIndex.method2113();
                        var2 += CacheIndex.itemConfigIndex.method2136((byte) -128);
                        CacheIndex.animationIndex.method2113();
                        var2 += CacheIndex.animationIndex.method2136((byte) -122);
                        CacheIndex.graphicFXIndex.method2113();
                        var2 += CacheIndex.graphicFXIndex.method2136((byte) -122);
                        CacheIndex.clientScriptConfigIndex.method2113();
                        var2 += CacheIndex.clientScriptConfigIndex.method2136((byte) -128);
                        CacheIndex.quickchatMessagesIndex.method2113();
                        var2 += CacheIndex.quickchatMessagesIndex.method2136((byte) -126);
                        CacheIndex.quickchatMenusIndex.method2113();
                        var2 += CacheIndex.quickchatMenusIndex.method2136((byte) -128);
                        CacheIndex.particlesConfigIndex.method2113();
                        var2 += CacheIndex.particlesConfigIndex.method2136((byte) -124);
                        if (var2 >= 1100) {
                            Class132.method1799((byte) 96, CacheIndex.configurationsIndex);
                            method631(CacheIndex.configurationsIndex);
                            Class3_Sub28_Sub8.method575(CacheIndex.configurationsIndex, -1);
                            method375(CacheIndex.modelsIndex, CacheIndex.configurationsIndex);
                            Class108.method1661(CacheIndex.objectConfigIndex, CacheIndex.modelsIndex);
                            ItemDefinition.method1103(CacheIndex.modelsIndex, CacheIndex.npcConfigIndex);
                            GameObject.method1864(CacheIndex.itemConfigIndex, Class157.aClass3_Sub28_Sub17_Sub1_2000, CacheIndex.modelsIndex);
                            StructDefinitionProvider.setIndex(CacheIndex.configurationsIndex);
                            Class3_Sub20.method392(CacheIndex.skinsIndex, CacheIndex.animationIndex, CacheIndex.skeletonsIndex);
                            Unsorted.method1053(CacheIndex.configurationsIndex);
                            Class158.method2180(CacheIndex.modelsIndex, CacheIndex.graphicFXIndex);
                            Class107.method1648(CacheIndex.clientScriptConfigIndex);
                            LinkableRSString.method731(CacheIndex.configurationsIndex);
                            Unsorted.method89(CacheIndex.fontsIndex, CacheIndex.spritesIndex, CacheIndex.interfacesIndex, CacheIndex.modelsIndex);
                            TextureOperation23.method250(CacheIndex.configurationsIndex);
                            EnumDefinitionProvider.setIndex(CacheIndex.clientscriptMaskIndex);
                            QuickChat.method205(CacheIndex.quickchatMenusIndex, CacheIndex.quickchatMessagesIndex, new Class7());
                            QuickChat.method1236(CacheIndex.quickchatMenusIndex, CacheIndex.quickchatMessagesIndex);
                            Class58.method1197(CacheIndex.configurationsIndex);
                            Unsorted.method2065(CacheIndex.configurationsIndex, CacheIndex.spritesIndex);
                            Class107.method1645(CacheIndex.configurationsIndex, CacheIndex.spritesIndex);
                            LoadingStageNumber = 50;
                            loadingBarTextToDisplay = TextCore.LoadedConfig;
                            Class29.method968(128);
                            anInt1354 = 80;
                        } else {
                            loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingConfig, RSString.stringAnimator(var2 / 11), RSString.parse("(U")});
                            LoadingStageNumber = 50;
                        }
                    } else if (anInt1354 == 80) {
                        var2 = Sprites.method107(CacheIndex.spritesIndex);
                        var3 = 15;
                        if (var2 < var3) {
                            loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingSprites, RSString.stringAnimator(var2 * 100 / var3), RSString.parse("(U")});
                            LoadingStageNumber = 60;
                        } else {
                            Sprites.method887(CacheIndex.spritesIndex);
                            anInt1354 = 90;
                            LoadingStageNumber = 60;
                            loadingBarTextToDisplay = TextCore.LoadedSprites;
                        }
                    } else if (anInt1354 != 90) {
                        if (anInt1354 == 100) {
                            if (TextureOperation25.method334(CacheIndex.spritesIndex)) {
                                anInt1354 = 110;
                            }
                        } else if (anInt1354 == 110) {
                            TextureOperation20.aClass67_1443 = new Class67();
                            Class38.gameSignlink.startThread(10, TextureOperation20.aClass67_1443);
                            loadingBarTextToDisplay = TextCore.LoadedInputHandler;
                            LoadingStageNumber = 75;
                            anInt1354 = 120;
                        } else if (anInt1354 != 120) {
                            if (anInt1354 == 130) {
                                if (CacheIndex.interfacesIndex.method2113()) {
                                    if (CacheIndex.interfaceScriptsIndex.method2113()) {
                                        if (CacheIndex.fontsIndex.method2113()) {
                                            if (CacheIndex.worldmapIndex.method2127(TextCore.aClass94_1342)) {
                                                Class75_Sub4.method1353(Sprites.aClass3_Sub28_Sub16_Sub2Array2140, CacheIndex.worldmapIndex);
                                                LoadingStageNumber = 95;
                                                loadingBarTextToDisplay = TextCore.LoadedInterfaces;
                                                anInt1354 = 135;
                                            } else {
                                                loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingInterfaces, RSString.stringAnimator(90 - -(CacheIndex.worldmapIndex.method2116(TextCore.aClass94_1342) / 10)), RSString.parse("(U")});
                                                LoadingStageNumber = 85;
                                            }
                                        } else {
                                            loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingInterfaces, RSString.stringAnimator(85 - -(CacheIndex.fontsIndex.method2136((byte) -124) / 20)), RSString.parse("(U")});
                                            LoadingStageNumber = 85;
                                        }
                                    } else {
                                        loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingInterfaces, RSString.stringAnimator(75 - -(CacheIndex.interfaceScriptsIndex.method2136((byte) -128) / 10)), RSString.parse("(U")});
                                        LoadingStageNumber = 85;
                                    }
                                } else {
                                    loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingInterfaces, RSString.stringAnimator(CacheIndex.interfacesIndex.method2136((byte) -123) * 3 / 4), RSString.parse("(U")});
                                    LoadingStageNumber = 85;
                                }
                            } else if (135 == anInt1354) {
                                var2 = Class121.method1735();
                                if (-1 == var2) {
                                    LoadingStageNumber = 95;
                                    loadingBarTextToDisplay = TextCore.LoadingWLD;
                                } else if (var2 == 7 || var2 == 9) {
                                    this.errorPrint("worldlistfull");
                                    Class117.method1719(1000);
                                } else if (Class30.loadedWorldList) {
                                    loadingBarTextToDisplay = TextCore.LoadedWLD;
                                    anInt1354 = 140;
                                    LoadingStageNumber = 96;
                                } else {
                                    this.errorPrint("worldlistio_" + var2);
                                    Class117.method1719(1000);
                                }
                            } else if (anInt1354 == 140) {
                                loginScreenInterfaceID = CacheIndex.interfacesIndex.getArchiveForName(RSString.parse("loginscreen"));
                                CacheIndex.landscapesIndex.method2115(-9, false);
                                CacheIndex.musicIndex.method2115(111, false);//true
                                CacheIndex.spritesIndex.method2115(-76, true);
                                CacheIndex.fontsIndex.method2115(91, true);
                                CacheIndex.huffmanEncodingIndex.method2115(-116, true);
                                CacheIndex.interfacesIndex.method2115(99, true);
                                LoadingStageNumber = 97;
                                loadingBarTextToDisplay = TextCore.Starting3DLibrary;
                                anInt1354 = 150;
                                sweepReferenceCache = true;
                            } else if (anInt1354 == 150) {
                                Class88.method1454();
                                if (Unsorted.aBoolean2146) {
                                    Class3_Sub28_Sub9.anInt3622 = 0;
                                    Unsorted.anInt3671 = 0;
                                    Unsorted.anInt2577 = 0;
                                    Class3_Sub20.anInt2488 = 0;
                                }

                                Unsorted.aBoolean2146 = true;
                                Class119.method1730(Class38.gameSignlink);
                                GameObject.graphicsSettings(false, Unsorted.anInt2577, -1, -1);
                                LoadingStageNumber = 100;
                                anInt1354 = 160;
                                loadingBarTextToDisplay = TextCore.Started3DLibrary;
                            } else if (anInt1354 == 160) {
                                TextureOperation1.method219(true);
                            }
                        } else if (CacheIndex.huffmanEncodingIndex.method2125(RSString.parse(""), TextCore.HasHuffman)) {
                            Class36 var9 = new Class36(CacheIndex.huffmanEncodingIndex.method2123(RSString.parse(""), TextCore.HasHuffman));
                            Class1.method69(var9);
                            loadingBarTextToDisplay = TextCore.LoadedWordPack;
                            anInt1354 = 130;
                            LoadingStageNumber = 80;
                        } else {
                            loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingWordPack, TextCore.aClass94_37});
                            LoadingStageNumber = 80;
                        }
                    } else if (CacheIndex.materialsIndex.method2113()) {
                        Class102 var8 = new Class102(CacheIndex.texturesIndex, CacheIndex.materialsIndex, CacheIndex.spritesIndex, !Class25.aBoolean488);
                        Class51.method1140(var8);
                        if (Unsorted.anInt3625 == 1) {
                            Class51.method1137(0.9F);
                        }

                        if (2 == Unsorted.anInt3625) {
                            Class51.method1137(0.8F);
                        }

                        if (Unsorted.anInt3625 == 3) {
                            Class51.method1137(0.7F);
                        }

                        if (Unsorted.anInt3625 == 4) {
                            Class51.method1137(0.6F);
                        }

                        loadingBarTextToDisplay = TextCore.LoadedTextures;
                        anInt1354 = 100;
                        LoadingStageNumber = 70;
                    } else {
                        loadingBarTextToDisplay = RSString.stringCombiner(new RSString[]{TextCore.LoadingTextures, RSString.stringAnimator(CacheIndex.materialsIndex.method2136((byte) -125)), RSString.parse("(U")});
                        LoadingStageNumber = 70;
                    }
                }
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "client.A(" + var1 + ')');
        }
    }

    final void method25() {
        try {
            if (Class143.gameStage != 1000) {
                ++Class44.anInt719;
                if (Class44.anInt719 % 1000 == 1) {
                    GregorianCalendar var2 = new GregorianCalendar();
                    Class38_Sub1.anInt2618 = var2.get(Calendar.HOUR_OF_DAY) * 600 - (-(var2.get(Calendar.MINUTE) * 10) + -(var2.get(Calendar.SECOND) / 6));
                    Class24.aRandom3088.setSeed(Class38_Sub1.anInt2618);
                }

                this.method48();
                if (Unsorted.aClass8_1936 != null) {
                    Unsorted.aClass8_1936.method838();
                }

                LinkableRSString.method728();
                Class58.method1194();
                Class32.method996();
                Unsorted.method1225();
                if (HDToolKit.highDetail) {
                    Class31.method990();
                }

                int var4;
                if (Class38.aClass146_668 != null) {
                    var4 = Class38.aClass146_668.method2078();
                    Class29.anInt561 = var4;
                }

                if (Class143.gameStage == 0) {
                    this.method52(48);
                    Class75_Sub4.method1355();
                } else if (Class143.gameStage == 5) {
                    this.method52(107);
                    Class75_Sub4.method1355();
                } else if (Class143.gameStage == 25 || Class143.gameStage == 28) {
                    Class40.method1046();
                }

                if (10 == Class143.gameStage) {
                    this.method47();
                    TextureOperation37.method267();
                    Class163_Sub1_Sub1.method2216();
                    LoginHandler.handleLogin();
                } else if (Class143.gameStage == 30) {
                    TextureOperation20.method235();
                } else if (Class143.gameStage == 40) {
                    LoginHandler.handleLogin();
                    if (messageToDisplay != -3) {
                        if (messageToDisplay == 15) {
                            Class21.method912();
                        } else if (messageToDisplay != 2) {
                            Class167.method2269((byte) 46);
                        }
                    }
                }

            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "client.N(" + (byte) 107 + ')');
        }
    }
}
