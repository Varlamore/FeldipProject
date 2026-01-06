package com.cryptic.cache.graphics.font;

public class CP1252UTF8 {
    public static final char[] CP1252_TABLE = new char[]{'€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000', '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'};
    public static String totp;

    CP1252UTF8() throws Throwable {
        throw new Error();
    }

    public static String cp1252_to_utf8(byte[] arg0, int arg1, int arg2) {
        char[] var4 = new char[arg2];
        int var5 = 0;

        for(int var6 = 0; var6 < arg2; ++var6) {
            int var7 = arg0[arg1 + var6] & 255;
            if (var7 != 0) {
                if (var7 >= 128 && var7 < 160) {
                    char var8 = CP1252_TABLE[var7 - 128];
                    if (var8 == 0) {
                        var8 = '?';
                    }

                    var7 = var8;
                }

                var4[var5++] = (char)var7;
            }
        }

        return new String(var4, 0, var5);
    }
}