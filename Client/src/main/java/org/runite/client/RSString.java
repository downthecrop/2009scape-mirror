package org.runite.client;

import org.rs09.client.config.GameConfig;

import org.rs09.client.util.ArrayUtils;
import org.rs09.client.data.HashTable;

import java.applet.Applet;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class RSString implements Interface3 {

    static HashTable<LinkableRSString> interned;
    private boolean mutable = true;
    byte[] buffer;
    int length;

    /**
     * @return A RSString consisting of the actual bytes in the provided string.
     */
    public static RSString of(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.ISO_8859_1);

        RSString rs = new RSString();
        rs.buffer = bytes;
        rs.length = 0;

        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] != 0) {
                bytes[rs.length++] = bytes[i];
            }
        }

        return rs;
    }

    /**
     * @return A RSString that is interned and parsed.
     */
    public static RSString parse(String string) {
        byte[] bytes = Objects.requireNonNull(string).replace("RuneScape", GameConfig.SERVER_NAME).getBytes();

        int length = bytes.length;
        RSString jagString = new RSString();
        jagString.buffer = new byte[length];

        int i = 0;
        while (length > i) {
            int ascii = bytes[i++] & 255;
            if (ascii <= 45 && ascii >= 40) {
                if (length <= i) {
                    break;
                }

                int var7 = bytes[i++] & 0xff;
                jagString.buffer[jagString.length++] = (byte) (-48 + var7 + 43 * (-40 + ascii));
            } else if (ascii != 0) {
                jagString.buffer[jagString.length++] = (byte) ascii;
            }
        }
        jagString.method1576();
        return jagString.intern();
    }

    static RSString stringCombiner(RSString[] var0) {
        if (var0.length >= 2) {
            return Class67.method1261(0, var0.length, var0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    static RSString stringAnimator(int var1) {
       try {
          return Unsorted.method1723((byte)-117, false, var1);
       } catch (RuntimeException var3) {
          throw ClientErrorException.clientError(var3, "jj.C(" + var1 + ')');
       }
    }

    public final URL toURL() throws MalformedURLException {
        return new URL(new String(this.buffer, 0, this.length));
    }

    public final boolean equalsStringIgnoreCase(RSString other) {
        if (other == null) {
            return false;
        } else if (this.length == other.length) {

            for (int i = 0; i < this.length; ++i) {
                byte var5 = this.buffer[i];
                if (var5 >= 65 && var5 <= 90 || var5 >= -64 && var5 <= -34 && var5 != -41) {
                    var5 = (byte) (var5 + 32);
                }

                byte var6 = other.buffer[i];
                if (65 <= var6 && var6 <= 90 || -64 <= var6 && var6 <= -34 && var6 != -41) {
                    var6 = (byte) (var6 + 32);
                }

                if (var6 != var5) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    // TODO Refactor to support regular equals method
    public final boolean equalsString(RSString other) {
        if (other == null) {
            return false;
        } else if (this == other) {
            return true;
        } else if (this.length == other.length) {

            byte[] otherBuffer = other.buffer;
            byte[] thisBuffer = this.buffer;

            for (int i = 0; i < this.length; ++i) {
                if (thisBuffer[i] != otherBuffer[i]) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public final int parseInt(int radix) {
        if (radix < 1 || radix > 36) {
            radix = 10;
        }

        boolean negate = false;
        boolean success = false;
        int value = 0;

        for (int i = 0; this.length > i; ++i) {
            int currentNumber = this.buffer[i] & 0xff;

            if (i == 0) {
                if (currentNumber == '-') {
                    negate = true;
                    continue;
                }

                if (currentNumber == '+') {
                    continue;
                }
            }

            if (currentNumber >= '0' && '9' >= currentNumber) {
                currentNumber -= '0';
            } else if ('A' <= currentNumber && currentNumber <= 'Z') {
                currentNumber -= 55;
            } else {
                if (currentNumber < 'a' || currentNumber > 'z') {
                    throw new NumberFormatException("invalid character in number with value: " + currentNumber + " ('" + ((char) currentNumber) + "'). This string: " + toString());
                }

                currentNumber -= 87;
            }

            if (currentNumber >= radix) {
                throw new NumberFormatException("got radix " + radix + ", but found number value " + currentNumber);
            }

            if (negate) {
                currentNumber = -currentNumber;
            }

            int newValue = currentNumber + value * radix;
            if (newValue / radix != value) {
                throw new NumberFormatException("integer overflow");
            }

            value = newValue;
            success = true;
        }

        if (success) {
            return value;
        } else {
            throw new NumberFormatException("failed to parse number");
        }
    }

    public final void drawString(Graphics g, int y, int x) {
        String string = new String(this.buffer, 0, this.length, StandardCharsets.ISO_8859_1);
        g.drawString(string, x, y);
    }

    final void append(RSString var1) {
        if (this.mutable) {
            if (var1.length + this.length > this.buffer.length) {
                int var3;
                for (var3 = 1; var3 < var1.length + this.length; var3 += var3) {
                }

                byte[] var4 = new byte[var3];
                ArrayUtils.arraycopy(this.buffer, 0, var4, 0, this.length);
                this.buffer = var4;
            }

            ArrayUtils.arraycopy(var1.buffer, 0, this.buffer, this.length, var1.length);
            this.length += var1.length;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public final RSString toLowercase() {
        RSString lower = new RSString();
        lower.length = this.length;
        lower.buffer = new byte[this.length];

        for (int i = 0; i < this.length; ++i) {
            byte ch = this.buffer[i];
            if (65 <= ch && ch <= 90 || ch >= -64 && ch <= -34 && ch != -41) {
                ch = (byte) (ch + 32);
            }

            lower.buffer[i] = ch;
        }

        return lower;
    }

    /**
     * Returns a copy of this string with proper capitalization. Generally, this returns an all-lower string. Characters
     * are changed to uppercase based on the following rules:
     * - The character is the first character in the string
     * - The character is the first non-whitespace character after a '.', '!' or '?' character
     *
     * Characters may be either lowercase or uppercase if the previous character is a whitespace character.
     *
     * @return A copy of this string with capitalization based on the rules above.
     */
    public final RSString properlyCapitalize() {
        byte state = 2;
        RSString newString = new RSString();
        newString.length = this.length;
        newString.buffer = new byte[this.length];

        for (int i = 0; i < this.length; ++i) {
            byte ch = this.buffer[i];
            if ((ch < 97 || 122 < ch) && (ch < -32 || ch > -2 || ch == -9)) { // if not lowercase chars
                if ((ch < 65 || ch > 90) && (ch < -64 || ch > -34 || ch == -41)) { // if not uppercase chars
                    if (ch != 46 && 33 != ch && ch != 63) { // if not `.`, `!` or `?`
                        if (32 == ch) { // if ' '
                            if (state != 2) {
                                state = 1;
                            }
                        } else { // if not ' '
                            state = 1;
                        }
                    } else { // if `.`, `!` or `?`
                        state = 2;
                    }
                } else { // if uppercase char
                    if (0 == state) {
                        ch = (byte) (ch + 32);
                    }

                    state = 0;
                }
            } else {
                if (state == 2) {
                    ch = (byte) (ch - 32);
                }

                state = 0;
            }

            newString.buffer[i] = ch;
        }

        return newString;
    }

    public final long hash() {
        long hash = 0L;
        for (int i = 0; i < this.length; ++i) {
            hash = (long) (this.buffer[i] & 0xff) + (hash << 5) - hash;
        }

        return hash;
    }

    public final int length() {
        return this.length;
    }

    public final RSString method1542(RSString var2, int var3, int var4) {
        if (!this.mutable) {
            throw new IllegalArgumentException();
        } else if (0 <= var3 && var3 <= var4 && var2.length >= var4) {
            if (this.length + (var4 - var3) > this.buffer.length) {
                int var5;
                for (var5 = 1; var5 < this.length + var2.length; var5 += var5) {
                }

                byte[] var6 = new byte[var5];
                ArrayUtils.arraycopy(this.buffer, 0, var6, 0, this.length);
                this.buffer = var6;
            }

            ArrayUtils.arraycopy(var2.buffer, var3, this.buffer, this.length, -var3 + var4);
            this.length += var4 + -var3;
            return this;
        } else {
            throw new IllegalArgumentException();
        }
    }

    final boolean isInteger() {
        return this.isInteger(10);
    }

    final RSString method1544(boolean var1) {
        try {
            RSString var2 = new RSString();
            var2.length = this.length;
            var2.buffer = new byte[var2.length];
            if (var1) {
                for (int var3 = 0; this.length > var3; ++var3) {
                    var2.buffer[this.length - var3 + -1] = this.buffer[var3];
                }

                return var2;
            } else {
                return (RSString) null;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "na.FB(" + var1 + ')');
        }
    }

    final RSString longToRSString() {
        try {
            RSString var2 = new RSString();
            var2.length = this.length;
            var2.buffer = new byte[this.length];
            boolean var3 = true;
            int var4 = 0;

            for (; var4 < this.length; ++var4) {
                byte var5 = this.buffer[var4];
                if (var5 == 95) {
                    var3 = true;
                    var2.buffer[var4] = 32;
                } else if (97 <= var5 && var5 <= 122 && var3) {
                    var3 = false;
                    var2.buffer[var4] = (byte) (-32 + var5);
                } else {
                    var2.buffer[var4] = var5;
                    var3 = false;
                }
            }

            return var2;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "na.G(" + (byte) -50 + ')');
        }
    }

    final int method1546(RSString var2) {
        int var3 = 0;
        int var4 = 0;
        int var6 = var2.length;
        int var5 = this.length;
        int var7 = this.length;
        int var8 = var2.length;
        int var9 = 0;
        int var10 = 0;

        while (var5 != 0 && var6 != 0) {
            if (var3 == 156 || var3 == 230) {
                var3 = 101;
            } else if (140 == var3 || var3 == 198) {
                var3 = 69;
            } else if (var3 == 223) {
                var3 = 115;
            } else {
                var3 = this.buffer[var9] & 255;
                ++var9;
            }

            if (Unsorted.method2103(var3, -116)) {
                ++var7;
            } else {
                --var5;
            }

            if (var4 == 156 || 230 == var4) {
                var4 = 101;
            } else if (var4 == 140 || var4 == 198) {
                var4 = 69;
            } else if (223 == var4) {
                var4 = 115;
            } else {
                var4 = 255 & var2.buffer[var10];
                ++var10;
            }

            if (Unsorted.method2103(var4, -86)) {
                ++var8;
            } else {
                --var6;
            }

            if (Class158.anIntArray2004[var4] > Class158.anIntArray2004[var3]) {
                return -1;
            }

            if (Class158.anIntArray2004[var3] > Class158.anIntArray2004[var4]) {
                return 1;
            }
        }

        return var8 <= var7 ? (var7 > var8 ? 1 : 0) : -1;
    }

    final URL method1547(URL var1) throws MalformedURLException {
        try {

            return new URL(var1, new String(this.buffer, 0, this.length));
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "na.EB(" + (var1 != null ? "{...}" : "null") + ',' + true + ')');
        }
    }

    final RSString method1548(int var2) {
        try {
            if (var2 > 0 && var2 <= 255) {
                RSString var3 = new RSString();
                var3.buffer = new byte[1 + this.length];
                var3.length = this.length + 1;
                ArrayUtils.arraycopy(this.buffer, 0, var3.buffer, 0, this.length);
                var3.buffer[this.length] = (byte) var2;
                return var3;
            } else {
                throw new IllegalArgumentException("invalid char");
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "na.OA(" + false + ',' + var2 + ')');
        }
    }

    final boolean endsWith(RSString var2) {
        if (var2.length > this.length) {
            return false;
        } else {
            int var3 = -var2.length + this.length;

            for (int var4 = 0; var4 < var2.length; ++var4) {
                if (this.buffer[var3 + var4] != var2.buffer[var4]) {
                    return false;
                }
            }

            return true;
        }
    }

    final int indexOf(RSString var1, int var2) {
        try {
            return var2 <= 49 ? -20 : this.method1566(var1, 0);
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "na.A(" + (var1 != null ? "{...}" : "null") + ',' + var2 + ')');
        }
    }

    public final int parseInt() {
        return this.parseInt(10);
    }

    final void method1553(int var1) {
        try {
            if (!this.mutable) {
                throw new IllegalArgumentException();
            } else if (var1 < 0) {
                throw new IllegalArgumentException();
            } else {
                int var3;
                if (this.buffer.length < var1) {
                    for (var3 = 1; var1 > var3; var3 += var3) {
                    }

                    byte[] var4 = new byte[var3];
                    ArrayUtils.arraycopy(this.buffer, 0, var4, 0, this.length);
                    this.buffer = var4;
                }

                for (var3 = this.length; var1 > var3; ++var3) {
                    this.buffer[var3] = 32;
                }

                this.length = var1;
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "na.RA(" + var1 + ',' + false + ')');
        }
    }

    public final String toString() {
        if (buffer == null) {
            throw new RuntimeException();
        }
        return new String(buffer);
    }

    final void method1554(Applet var2) throws Throwable {
        try {
            String var3 = new String(this.buffer, 0, this.length);
            Class42.method1057(var2, var3);
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "na.AA(" + true + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    final int method1555(int var1, int var2) {
        try {
            byte var4 = (byte) var1;
            for (int var5 = var2; this.length > var5; ++var5) {
                if (this.buffer[var5] == var4) {
                    return var5;
                }
            }

            return -1;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "na.NA(" + var1 + ',' + var2 + ',' + 1536 + ')');
        }
    }

    final RSString substring(int start) {
        return this.substring(start, this.length, (byte) -74 ^ -74);
    }

    // TODO Why is `dstpos` a thing? what good does it serve?
    public final RSString substring(int start, int end, int dstpos) {
        RSString subs = new RSString();
        subs.length = end - start;
        subs.buffer = new byte[end - start];
        ArrayUtils.arraycopy(this.buffer, start, subs.buffer, dstpos, subs.length);
        return subs;
    }

    final boolean startsWith(RSString other) {
        if (other.length > this.length) {
            return false;
        }

        for (int i = 0; i < other.length; ++i) {
            if (other.buffer[i] != this.buffer[i]) {
                return false;
            }
        }

        return true;
    }

    public final boolean equals(Object var1) {
        try {
            if (var1 instanceof RSString) {
                return this.equalsString((RSString) var1);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "na.equals(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    final int method1559(RSString var1) {
        try {

            int var3;
            if (var1.length < this.length) {
                var3 = var1.length;
            } else {
                var3 = this.length;
            }

            for (int var4 = 0; var3 > var4; ++var4) {
                if ((255 & this.buffer[var4]) < (var1.buffer[var4] & 255)) {
                    return -1;
                }

                if ((this.buffer[var4] & 255) > (var1.buffer[var4] & 255)) {
                    return 1;
                }
            }

            if (var1.length > this.length) {
                return -1;
            } else if (this.length <= var1.length) {
                return 0;
            } else {
                return 1;
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "na.QA(" + (var1 != null ? "{...}" : "null") + ',' + -1 + ')');
        }
    }

    final RSString method1560(RSString var1, RSString var3) {
        try {
            int var4 = this.length;
            int var5 = var1.length - var3.length;
            int var6 = 0;

            while (true) {
                int var7 = this.method1566(var3, var6);
                if (0 > var7) {
                    var6 = 0;
                    RSString var10 = Unsorted.emptyString(var4);

                    while (true) {
                        int var8 = this.method1566(var3, var6);
                        if (0 > var8) {
                            while (this.length > var6) {
                                Objects.requireNonNull(var10).appendCharacter(255 & this.buffer[var6++]);
                            }

                            if (false) {
                                this.method1567(-5, (byte) -91);
                            }

                            return var10;
                        }

                        while (var6 < var8) {
                            Objects.requireNonNull(var10).appendCharacter(this.buffer[var6++] & 255);
                        }

                        Objects.requireNonNull(var10).append(var1);
                        var6 += var3.length;
                    }
                }

                var6 = var7 - -var3.length;
                var4 += var5;
            }
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "na.IA(" + (var1 != null ? "{...}" : "null") + ',' + true + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

    public final int hashCode() {
        try {
            return this.method1574();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "na.hashCode()");
        }
    }

    private boolean isInteger(int radix) {
        if (radix < 1 || radix > 36) {
            radix = 10;
        }

        boolean success = false;
        boolean negate = false;
        int value = 0;

        for (int i = 0; i < this.length; ++i) {
            int current = this.buffer[i] & 255;
            if (0 == i) {
                if (current == 45) {
                    negate = true;
                    continue;
                }

                if (current == 43) {
                    continue;
                }
            }

            if (current >= 48 && current <= 57) {
                current -= 48;
            } else if (current >= 65 && current <= 90) {
                current -= 55;
            } else {
                if (97 > current || current > 122) {
                    return false;
                }

                current -= 87;
            }

            if (radix <= current) {
                return false;
            }

            if (negate) {
                current = -current;
            }

            int newValue = current + radix * value;
            if (newValue / radix != value) {
                return false;
            }

            value = newValue;
            success = true;
        }

        return success;
    }

    final boolean method1562(byte var1, RSString var2) {
        try {
            if (this.length < var2.length) {
                return false;
            } else {
                if (var1 != -32) {
                    this.length = 13;
                }

                for (int var3 = 0; var2.length > var3; ++var3) {
                    byte var4 = this.buffer[var3];
                    byte var5 = var2.buffer[var3];
                    if (var5 >= 65 && var5 <= 90 || -64 <= var5 && -34 >= var5 && -41 != var5) {
                        var5 = (byte) (var5 + 32);
                    }

                    if (65 <= var4 && var4 <= 90 || var4 >= -64 && -34 >= var4 && var4 != -41) {
                        var4 = (byte) (var4 + 32);
                    }

                    if (var5 != var4) {
                        return false;
                    }
                }

                return true;
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "na.HB(" + var1 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    final RSString method1563(int var1) {
        try {
            if (var1 <= 86) {
                this.trim(117);
            }

            return this;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "na.K(" + var1 + ')');
        }
    }

    final RSString trim(int var1) {
        try {
            if (var1 != 1) {
                Unsorted.method1535((WorldListEntry) null, (WorldListEntry) null, 23, 68, 126, false, false);
            }

            int var2;
            for (var2 = 0; var2 < this.length && (0 <= this.buffer[var2] && 32 >= this.buffer[var2] || (255 & this.buffer[var2]) == 160); ++var2) {
            }

            int var3;
            for (var3 = this.length; var3 > var2 && (this.buffer[var3 - 1] >= 0 && this.buffer[var3 - 1] <= 32 || (255 & this.buffer[var3 + -1]) == 160); --var3) {
            }

            if (var2 == 0 && var3 == this.length) {
                return this;
            } else {
                RSString var4 = new RSString();
                var4.length = var3 + -var2;
                var4.buffer = new byte[var4.length];

                if (var4.length >= 0) System.arraycopy(this.buffer, var2, var4.buffer, 0, var4.length);

                return var4;
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "na.KA(" + var1 + ')');
        }
    }

    final RSString method1565() {
        try {
            byte var4 = (byte) 47;
            RSString var6 = new RSString();
            byte var5 = (byte) 32;
            var6.length = this.length;
            var6.buffer = new byte[this.length];

            for (int var7 = 0; var7 < this.length; ++var7) {
                byte var8 = this.buffer[var7];
                if (var4 == var8) {
                    var6.buffer[var7] = var5;
                } else {
                    var6.buffer[var7] = var8;
                }
            }

            return var6;
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "na.HA(" + 32 + ',' + 40 + ',' + 47 + ')');
        }
    }

    final int method1566(RSString var1, int var2) {
        try {
            int var4 = var1.length;
            if (var2 >= this.length) {
                return var4 == 0 ? this.length : -1;
            } else {
                if (var2 < 0) {
                    var2 = 0;
                }

                if (var4 == 0) {
                    return var2;
                } else {
                    int var7 = this.length - var4;
                    byte[] var5 = var1.buffer;
                    byte var6 = var5[0];
                    int var8 = var2;

                    while (var7 >= var8) {
                        if (this.buffer[var8] != var6) {
                            do {
                                ++var8;
                                if (var8 > var7) {
                                    return -1;
                                }
                            } while (this.buffer[var8] != var6);
                        }

                        boolean var9 = true;
                        int var10 = 1 + var8;
                        int var11 = 1;

                        while (true) {
                            if (var11 < var4) {
                                if (this.buffer[var10] == var5[var11]) {
                                    ++var10;
                                    ++var11;
                                    continue;
                                }

                                var9 = false;
                            }

                            if (var9) {
                                return var8;
                            }

                            ++var8;
                            break;
                        }
                    }

                    return -1;
                }
            }
        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "na.CB(" + (var1 != null ? "{...}" : "null") + ',' + var2 + ',' + -1 + ')');
        }
    }

    final RSString[] method1567(int var1, byte var2) {
        try {
            int var3 = 0;

            for (int var4 = 0; var4 < this.length; ++var4) {
                if (this.buffer[var4] == var1) {
                    ++var3;
                }
            }

            RSString[] var11 = new RSString[1 + var3];
            if (var3 == 0) {
                var11[0] = this;
            } else {
                int var5 = 0;
                int var6 = 0;

                for (int var7 = 0; var3 > var7; ++var7) {
                    int var9;
                    for (var9 = 0; this.buffer[var9 + var6] != var1; ++var9) {
                    }

                    var11[var5++] = this.substring(var6, var6 - -var9, 0);
                    var6 += 1 + var9;
                }

                var11[var3] = this.substring(var6, this.length, 0);
            }
            return var11;
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "na.GA(" + var1 + ',' + var2 + ')');
        }
    }

    final byte[] method1568() {
        try {
            byte[] var2 = new byte[this.length];
            ArrayUtils.arraycopy(this.buffer, 0, var2, 0, this.length);
            return var2;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "na.H(" + 0 + ')');
        }
    }

    final int charAt(int var1, byte var2) {
        try {
            return this.buffer[var1] & 255;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "na.SA(" + var1 + ',' + var2 + ')');
        }
    }

    public final RSString intern() {
        final long hash = this.hash();
        synchronized (RSString.class) {
            LinkableRSString linkable;
            if (interned == null) {
                interned = new HashTable<>(4096);
            } else {
                for (linkable = interned.get(hash); null != linkable; linkable = interned.nextInBucket()) {
                    if (this.equalsString(linkable.value)) {
                        return linkable.value;
                    }
                }
            }

            linkable = new LinkableRSString();

            linkable.value = this;
            mutable = false;
            interned.put(hash, linkable);
        }
        return this;
    }

    final void appendCharacter(int character) {
        if (0 < character && character <= 255) {
            if (this.mutable) {
                if (this.length == this.buffer.length) {
                    // TODO Is this really just making a new buffer with the size + 1? Why do we have to loop for that?
                    int newBufferSize;
                    for (newBufferSize = 1; this.length >= newBufferSize; newBufferSize += newBufferSize) {
                    }

                    byte[] newBuffer = new byte[newBufferSize];
                    ArrayUtils.arraycopy(this.buffer, 0, newBuffer, 0, this.length);
                    this.buffer = newBuffer;
                }

                this.buffer[this.length++] = (byte) character;
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException("invalid char:" + character);
        }
    }

    final RSString getParamValue(Applet applet) {
        String string = new String(this.buffer, 0, this.length);
        String paramValue = applet.getParameter(string);
        return null == paramValue ? null : of(paramValue);
    }

    final int method1574() {
        try {
            int var2 = 0;

            for (int var3 = 0; var3 < this.length; ++var3) {
                var2 = (255 & this.buffer[var3]) + -var2 + (var2 << 5);
            }

            return var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "na.J(" + false + ')');
        }
    }

    final int method1575(FontMetrics var2) {
        try {
            String var3;
            var3 = new String(this.buffer, 0, this.length, StandardCharsets.ISO_8859_1);

            return var2.stringWidth(var3);
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "na.V(" + -21018 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    final RSString method1576() {
        try {
            if (this.mutable) {

                if (this.buffer.length != this.length) {
                    byte[] var2 = new byte[this.length];
                    ArrayUtils.arraycopy(this.buffer, 0, var2, 0, this.length);
                    this.buffer = var2;
                }

                return this;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "na.PA(" + (byte) 90 + ')');
        }
    }

    final Object method1577(Applet var2) throws Throwable {
        try {
            String var3 = new String(this.buffer, 0, this.length);
            /*Object var4 = Class42.method1055(var3, var2);
            if (var4 instanceof String) {
                byte[] var5 = ((String) var4).getBytes();
                var4 = Class3_Sub13_Sub3.method178(var5, var5.length, 0);
            }
*/
            return null;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "na.JA(" + -1857 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    final long toLong() {
        long var2 = 0L;
        for (int var4 = 0; var4 < this.length && var4 < 12; ++var4) {
            byte var5 = this.buffer[var4];
            var2 *= 37L;
            if (65 <= var5 && 90 >= var5) {
                var2 += -65 + 1 + var5;
            } else if (var5 >= 97 && 122 >= var5) {
                var2 += -97 + var5 + 1;
            } else if (var5 >= 48 && var5 <= 57) {
                var2 += -48 + var5 + 27;
            }
        }

        while (var2 % 37L == 0 && var2 != 0L) {
            var2 /= 37L;
        }

        return var2;
    }

    final RSString method1579() {
        try {
            RSString var2 = Unsorted.method1052(this.toLong());
            return null == var2 ? TextCore.aClass94_1760 : var2;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "na.Q(" + -17 + ')');
        }
    }

    public final int method1580(byte[] var2, int var3, int var5) {
        try {
            ArrayUtils.arraycopy(this.buffer, 0, var2, var3, var5);

            return var5;
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "na.LA(" + true + ',' + (var2 != null ? "{...}" : "null") + ',' + var3 + ',' + 0 + ',' + var5 + ')');
        }
    }

}
