package core.tools;

public class CP1252 {
    private static char[] charMap = {'\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020', '\u2021',
            '\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018', '\u2019',
            '\u201c', '\u201d', '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153',
            '\0', '\u017e', '\u0178'};

    public static char getFromByte(byte value) {
        int out = value & 0xff;
        if (out == 0) {
            throw new IllegalArgumentException("Non cp1252 character 0x" + Integer.toString(out, 16) + " provided");
        }
        if (out >= 128 && out < 160) {
            int cp1252 = charMap[out - 128];
            if (cp1252 == 0) {
                cp1252 = 63;
            }
            out = cp1252;
        }
        return (char) out;
    }
}
