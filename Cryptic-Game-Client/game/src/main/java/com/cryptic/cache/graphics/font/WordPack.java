package com.cryptic.cache.graphics.font;

import com.cryptic.io.Buffer;

public class WordPack {
    public static Huffman huffman;

    WordPack() throws Throwable {
        throw new Error();
    }

    public static char method2639(char arg0) {
        return arg0 != 181 && arg0 != 402 ? Character.toTitleCase(arg0) : arg0;
    }

    static String unpack(Buffer arg0, int arg1) {
        try {
            int var3 = arg0.get_unsignedsmart_byteorshort();
            if (var3 > arg1) {
                var3 = arg1;
            }

            byte[] var4 = new byte[var3];
            arg0.pos += huffman.decompress(arg0.payload, arg0.pos, var4, 0, var3);
            return CP1252UTF8.cp1252_to_utf8(var4, 0, var3);
        } catch (Exception var7) {
            return "Cabbage";
        }
    }

    public static byte[] method5149(CharSequence arg0) {
        int var2 = arg0.length();
        byte[] var3 = new byte[var2];

        for(int var4 = 0; var4 < var2; ++var4) {
            char var5 = arg0.charAt(var4);
            if (var5 > 0 && var5 < 128 || var5 >= 160 && var5 <= 255) {
                var3[var4] = (byte)var5;
            } else if (8364 == var5) {
                var3[var4] = -128;
            } else if (var5 == 8218) {
                var3[var4] = -126;
            } else if (402 == var5) {
                var3[var4] = -125;
            } else if (8222 == var5) {
                var3[var4] = -124;
            } else if (8230 == var5) {
                var3[var4] = -123;
            } else if (var5 == 8224) {
                var3[var4] = -122;
            } else if (8225 == var5) {
                var3[var4] = -121;
            } else if (var5 == 710) {
                var3[var4] = -120;
            } else if (8240 == var5) {
                var3[var4] = -119;
            } else if (var5 == 352) {
                var3[var4] = -118;
            } else if (var5 == 8249) {
                var3[var4] = -117;
            } else if (338 == var5) {
                var3[var4] = -116;
            } else if (var5 == 381) {
                var3[var4] = -114;
            } else if (8216 == var5) {
                var3[var4] = -111;
            } else if (8217 == var5) {
                var3[var4] = -110;
            } else if (var5 == 8220) {
                var3[var4] = -109;
            } else if (8221 == var5) {
                var3[var4] = -108;
            } else if (8226 == var5) {
                var3[var4] = -107;
            } else if (var5 == 8211) {
                var3[var4] = -106;
            } else if (8212 == var5) {
                var3[var4] = -105;
            } else if (732 == var5) {
                var3[var4] = -104;
            } else if (var5 == 8482) {
                var3[var4] = -103;
            } else if (var5 == 353) {
                var3[var4] = -102;
            } else if (8250 == var5) {
                var3[var4] = -101;
            } else if (var5 == 339) {
                var3[var4] = -100;
            } else if (var5 == 382) {
                var3[var4] = -98;
            } else if (var5 == 376) {
                var3[var4] = -97;
            } else {
                var3[var4] = 63;
            }
        }

        return var3;
    }

    public static String method2630(String arg0) {
        int var2 = arg0.length();
        char[] var3 = new char[var2];
        byte var4 = 2;

        for(int var5 = 0; var5 < var2; ++var5) {
            char var6 = arg0.charAt(var5);
            if (0 == var4) {
                var6 = Character.toLowerCase(var6);
            } else if (var4 == 2 || Character.isUpperCase(var6)) {
                var6 = WordPack.method2639(var6);
            }

            if (Character.isLetter(var6)) {
                var4 = 0;
            } else if (var6 != '.' && '?' != var6 && var6 != '!') {
                if (Character.isSpaceChar(var6)) {
                    if (2 != var4) {
                        var4 = 1;
                    }
                } else {
                    var4 = 1;
                }
            } else {
                var4 = 2;
            }

            var3[var5] = var6;
        }

        return new String(var3);
    }

    public static int pack(Buffer arg0, String arg1) {
        int var3 = arg0.pos;
        byte[] var4 = method5149(arg1);
        arg0.put_smart_byteshort(var4.length);
        arg0.pos += huffman.compress(var4, 0, var4.length, arg0.payload, arg0.pos);
        return arg0.pos - var3;
    }

    public static String decodeHuffman(Buffer arg0) {
        return unpack(arg0, 32767);
    }
}
