package com.cryptic.net.tcp.packet;

import com.cryptic.entity.model.HashLink;

import java.math.BigInteger;

public class Packet extends HashLink {
    public static int[] CRC32_TABLE = new int[256];
    public static long[] CRC64_TABLE;
    public byte[] array;
    public int pos;

    static {
        int var2;
        for(int var1 = 0; var1 < 256; ++var1) {
            int var0 = var1;

            for(var2 = 0; var2 < 8; ++var2) {
                if (1 == (var0 & 1)) {
                    var0 = var0 >>> 1 ^ -306674912;
                } else {
                    var0 >>>= 1;
                }
            }

            CRC32_TABLE[var1] = var0;
        }

        CRC64_TABLE = new long[256];

        for(var2 = 0; var2 < 256; ++var2) {
            long var4 = (long)var2;

            for(int var3 = 0; var3 < 8; ++var3) {
                if (1L == (var4 & 1L)) {
                    var4 = var4 >>> 1 ^ -3932672073523589310L;
                } else {
                    var4 >>>= 1;
                }
            }

            CRC64_TABLE[var2] = var4;
        }

    }

    public Packet(int arg0) {
        this.array = PacketPool.allocate(arg0);
        this.pos = 0;
    }

    public Packet(byte[] arg0) {
        this.array = arg0;
        this.pos = 0;
    }

    public void release() {
        if (this.array != null) {
            PacketPool.release(this.array);
        }

        this.array = null;
    }

    public void put_byte(int arg0) {
        this.array[++this.pos - 1] = (byte)arg0;
    }

    public void put_short(int arg0) {
        this.array[++this.pos - 1] = (byte)(arg0 >> 8);
        this.array[++this.pos - 1] = (byte)arg0;
    }

    public void put_mediumint(int arg0) {
        this.array[++this.pos - 1] = (byte)(arg0 >> 16);
        this.array[++this.pos - 1] = (byte)(arg0 >> 8);
        this.array[++this.pos - 1] = (byte)arg0;
    }

    public void put_int(int arg0) {
        this.array[++this.pos - 1] = (byte)(arg0 >> 24);
        this.array[++this.pos - 1] = (byte)(arg0 >> 16);
        this.array[++this.pos - 1] = (byte)(arg0 >> 8);
        this.array[++this.pos - 1] = (byte)arg0;
    }

    public void put_mediumlong(long arg0) {
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 40));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 32));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 24));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 16));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 8));
        this.array[++this.pos - 1] = (byte)((int)arg0);
    }

    public void put_long(long arg0) {
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 56));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 48));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 40));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 32));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 24));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 16));
        this.array[++this.pos - 1] = (byte)((int)(arg0 >> 8));
        this.array[++this.pos - 1] = (byte)((int)arg0);
    }

    public static int encodeStringCp1252(CharSequence arg0, int arg1, int arg2, byte[] arg3, int arg4) {
        int var6 = arg2 - arg1;

        for(int var7 = 0; var7 < var6; ++var7) {
            char var8 = arg0.charAt(arg1 + var7);
            if (var8 > 0 && var8 < 128 || var8 >= 160 && var8 <= 255) {
                arg3[arg4 + var7] = (byte)var8;
            } else if (8364 == var8) {
                arg3[arg4 + var7] = -128;
            } else if (8218 == var8) {
                arg3[var7 + arg4] = -126;
            } else if (402 == var8) {
                arg3[var7 + arg4] = -125;
            } else if (8222 == var8) {
                arg3[var7 + arg4] = -124;
            } else if (var8 == 8230) {
                arg3[var7 + arg4] = -123;
            } else if (var8 == 8224) {
                arg3[var7 + arg4] = -122;
            } else if (var8 == 8225) {
                arg3[arg4 + var7] = -121;
            } else if (var8 == 710) {
                arg3[var7 + arg4] = -120;
            } else if (var8 == 8240) {
                arg3[var7 + arg4] = -119;
            } else if (352 == var8) {
                arg3[var7 + arg4] = -118;
            } else if (8249 == var8) {
                arg3[var7 + arg4] = -117;
            } else if (var8 == 338) {
                arg3[arg4 + var7] = -116;
            } else if (var8 == 381) {
                arg3[var7 + arg4] = -114;
            } else if (8216 == var8) {
                arg3[var7 + arg4] = -111;
            } else if (8217 == var8) {
                arg3[var7 + arg4] = -110;
            } else if (var8 == 8220) {
                arg3[var7 + arg4] = -109;
            } else if (8221 == var8) {
                arg3[arg4 + var7] = -108;
            } else if (var8 == 8226) {
                arg3[arg4 + var7] = -107;
            } else if (var8 == 8211) {
                arg3[var7 + arg4] = -106;
            } else if (8212 == var8) {
                arg3[var7 + arg4] = -105;
            } else if (var8 == 732) {
                arg3[arg4 + var7] = -104;
            } else if (8482 == var8) {
                arg3[var7 + arg4] = -103;
            } else if (var8 == 353) {
                arg3[var7 + arg4] = -102;
            } else if (var8 == 8250) {
                arg3[var7 + arg4] = -101;
            } else if (339 == var8) {
                arg3[arg4 + var7] = -100;
            } else if (382 == var8) {
                arg3[var7 + arg4] = -98;
            } else if (376 == var8) {
                arg3[var7 + arg4] = -97;
            } else {
                arg3[var7 + arg4] = 63;
            }
        }

        return var6;
    }
    
    public void put_boolean(boolean arg0) {
        this.put_byte(arg0 ? 1 : 0);
    }

    public static int jstring_size(String val) {
        return val.length() + 1;
    }

    public void put_jstring(String arg0) {
        int ch = arg0.indexOf(0);
        if (ch >= 0) {
            throw new IllegalArgumentException("NUL character at " + ch + " - cannot pjstr");
        } else {
            this.pos += encodeStringCp1252(arg0, 0, arg0.length(), this.array, this.pos);
            this.array[++this.pos - 1] = 0;
        }
    }

    public void put_jstring_nullpadded(String arg0) {
        int ch = arg0.indexOf(0);
        if (ch >= 0) {
            throw new IllegalArgumentException("NUL character at " + ch + " - cannot pjstr2");
        } else {
            this.array[++this.pos - 1] = 0;
            this.pos += encodeStringCp1252(arg0, 0, arg0.length(), this.array, this.pos);
            this.array[++this.pos - 1] = 0;
        }
    }

    public void put_utf8(CharSequence arg0) {
        int var4 = arg0.length();
        int var5 = 0;

        int var6;
        for(var6 = 0; var6 < var4; ++var6) {
            char var7 = arg0.charAt(var6);
            if (var7 <= 127) {
                ++var5;
            } else if (var7 <= 2047) {
                var5 += 2;
            } else {
                var5 += 3;
            }
        }

        this.array[++this.pos - 1] = 0;
        this.put_var_int(var5);
        var5 = this.pos;
        byte[] var13 = this.array;
        int var8 = this.pos;
        int var9 = arg0.length();
        int var10 = var8;

        for(int var11 = 0; var11 < var9; ++var11) {
            char var12 = arg0.charAt(var11);
            if (var12 <= 127) {
                var13[var10++] = (byte)var12;
            } else if (var12 <= 2047) {
                var13[var10++] = (byte)(192 | var12 >> 6);
                var13[var10++] = (byte)(128 | var12 & 63);
            } else {
                var13[var10++] = (byte)(224 | var12 >> 12);
                var13[var10++] = (byte)(128 | var12 >> 6 & 63);
                var13[var10++] = (byte)(128 | var12 & 63);
            }
        }

        var6 = var10 - var8;
        this.pos = var6 + var5;
    }

    public void put_bytes(byte[] arg0, int arg1, int arg2) {
        for(int var5 = arg1; var5 < arg2 + arg1; ++var5) {
            this.array[++this.pos - 1] = arg0[var5];
        }

    }

    public void put(Packet arg0) {
        this.put_bytes(arg0.array, 0, arg0.pos);
    }

    public void put_size_int(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("psize4 out of range: " + size);
        } else {
            this.array[this.pos - size - 4] = (byte)(size >> 24);
            this.array[this.pos - size - 3] = (byte)(size >> 16);
            this.array[this.pos - size - 2] = (byte)(size >> 8);
            this.array[this.pos - size - 1] = (byte)size;
        }
    }

    public void put_size_short(int size) {
        if (size >= 0 && size <= 65535) {
            this.array[this.pos - size - 2] = (byte)(size >> 8);
            this.array[this.pos - size - 1] = (byte)size;
        } else {
            throw new IllegalArgumentException("psize2 out of range: " + size);
        }
    }

    public void put_size_byte(int size) {
        if (size >= 0 && size <= 255) {
            this.array[this.pos - size - 1] = (byte)size;
        } else {
            throw new IllegalArgumentException("psize1 out of range: " + size);
        }
    }

    public void put_smart_byteshort(int value) {
        if (value >= 0 && value < 128) {
            this.put_byte(value);
        } else if (value >= 0 && value < 32768) {
            this.put_short(value + 32768);
        } else {
            throw new IllegalArgumentException("psmarts out of range - val:" + value);
        }
    }

    public void put_var_int(int arg0) {
        if (0 != (arg0 & -128)) {
            if (0 != (arg0 & -16384)) {
                if ((arg0 & -2097152) != 0) {
                    if ((arg0 & -268435456) != 0) {
                        this.put_byte(arg0 >>> 28 | 128);
                    }

                    this.put_byte(arg0 >>> 21 | 128);
                }

                this.put_byte(arg0 >>> 14 | 128);
            }

            this.put_byte(arg0 >>> 7 | 128);
        }

        this.put_byte(arg0 & 127);
    }

    public int get_unsignedbyte() {
        return this.array[++this.pos - 1] & 255;
    }

    public byte get_byte() {
        return this.array[++this.pos - 1];
    }

    public int get_unsignedshort() {
        this.pos += 2;
        return (this.array[this.pos - 1] & 255) + ((this.array[this.pos - 2] & 255) << 8);
    }

    public int get_short() {
        this.pos += 2;
        int var2 = ((this.array[this.pos - 2] & 255) << 8) + (this.array[this.pos - 1] & 255);
        if (var2 > 32767) {
            var2 -= 65536;
        }

        return var2;
    }

    public int get_unsignedmediumint() {
        this.pos += 3;
        return ((this.array[this.pos - 3] & 255) << 16) + ((this.array[this.pos - 2] & 255) << 8) + (this.array[this.pos - 1] & 255);
    }

    public int get_mediumint() {
        this.pos += 3;
        int var2 = ((this.array[this.pos - 3] & 255) << 16) + ((this.array[this.pos - 2] & 255) << 8) + (this.array[this.pos - 1] & 255);
        if (var2 > 8388607) {
            var2 -= 16777216;
        }

        return var2;
    }

    public int get_int() {
        this.pos += 4;
        return ((this.array[this.pos - 2] & 255) << 8) + ((this.array[this.pos - 4] & 255) << 24) + ((this.array[this.pos - 3] & 255) << 16) + (this.array[this.pos - 1] & 255);
    }

    public long get_long() {
        long var2 = (long)this.get_int() & 4294967295L;
        long var4 = (long)this.get_int() & 4294967295L;
        return var4 + (var2 << 32);
    }

    public float get_float() {
        return Float.intBitsToFloat(this.get_int());
    }

    public boolean get_boolean() {
        return (this.get_unsignedbyte() & 1) == 1;
    }

    public String get_faststring() {
        if (this.array[this.pos] == 0) {
            ++this.pos;
            return null;
        } else {
            return this.get_string();
        }
    }

    public String get_string() {//gjstr
        int var2 = this.pos;

        while(this.array[++this.pos - 1] != 0) {
        }

        int var3 = this.pos - var2 - 1;
        return 0 == var3 ? "" : cp1252_to_utf8(this.array, var2, var3);
    }

    public static final char[] CP1252_TABLE = new char[]{'€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000', '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'};
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

    public String get_jstring() {//gjstr2 null padded
        byte var2 = this.array[++this.pos - 1];
        if (0 != var2) {
            throw new IllegalStateException("");
        } else {
            int var3 = this.pos;

            while(this.array[++this.pos - 1] != 0) {
            }

            int var4 = this.pos - var3 - 1;
            return var4 == 0 ? "" : cp1252_to_utf8(this.array, var3, var4);
        }
    }

    public String get_utf8() {
        byte var2 = this.array[++this.pos - 1];
        if (var2 != 0) {
            throw new IllegalStateException("");
        } else {
            int var3 = this.get_var_int();
            if (this.pos + var3 > this.array.length) {
                throw new IllegalStateException("");
            } else {
                byte[] var5 = this.array;
                int var6 = this.pos;
                char[] var7 = new char[var3];
                int var8 = 0;
                int var9 = var6;
                int var10 = var6 + var3;

                while(var9 < var10) {
                    int var11 = var5[var9++] & 255;
                    int var12;
                    if (var11 < 128) {
                        if (0 == var11) {
                            var12 = 65533;
                        } else {
                            var12 = var11;
                        }
                    } else if (var11 < 192) {
                        var12 = 65533;
                    } else if (var11 < 224) {
                        if (var9 < var10 && (var5[var9] & 192) == 128) {
                            var12 = (var11 & 31) << 6 | var5[var9++] & 63;
                            if (var12 < 128) {
                                var12 = 65533;
                            }
                        } else {
                            var12 = 65533;
                        }
                    } else if (var11 < 240) {
                        if (var9 + 1 < var10 && (var5[var9] & 192) == 128 && (var5[1 + var9] & 192) == 128) {
                            var12 = (var11 & 15) << 12 | (var5[var9++] & 63) << 6 | var5[var9++] & 63;
                            if (var12 < 2048) {
                                var12 = 65533;
                            }
                        } else {
                            var12 = 65533;
                        }
                    } else if (var11 < 248) {
                        if (2 + var9 < var10 && 128 == (var5[var9] & 192) && (var5[1 + var9] & 192) == 128 && 128 == (var5[var9 + 2] & 192)) {
                            var12 = (var11 & 7) << 18 | (var5[var9++] & 63) << 12 | (var5[var9++] & 63) << 6 | var5[var9++] & 63;
                            if (var12 >= 65536 && var12 <= 1114111) {
                                var12 = 65533;
                            } else {
                                var12 = 65533;
                            }
                        } else {
                            var12 = 65533;
                        }
                    } else {
                        var12 = 65533;
                    }

                    var7[var8++] = (char)var12;
                }

                String var4 = new String(var7, 0, var8);
                this.pos += var3;
                return var4;
            }
        }
    }

    public void get_bytes(byte[] arg0, int arg1, int arg2) {
        for(int var5 = arg1; var5 < arg2 + arg1; ++var5) {
            arg0[var5] = this.array[++this.pos - 1];
        }

    }

    public int get_smart_byteorshort() {
        int var2 = this.array[this.pos] & 255;
        return var2 < 128 ? this.get_unsignedbyte() - 64 : this.get_unsignedshort() - 49152;
    }

    public int get_unsignedsmart_byteorshort() {
        int var2 = this.array[this.pos] & 255;
        return var2 < 128 ? this.get_unsignedbyte() : this.get_unsignedshort() - 32768;
    }

    public int get_unsignedsmart_byteorshort_null() {
        int var2 = this.array[this.pos] & 255;
        return var2 < 128 ? this.get_unsignedbyte() - 1 : this.get_unsignedshort() - 32769;
    }

    public int get_unsignedsmart_byteorshort_increments() {
        int var2 = 0;

        int var3;
        for(var3 = this.get_unsignedsmart_byteorshort(); var3 == 32767; var3 = this.get_unsignedsmart_byteorshort()) {
            var2 += 32767;
        }

        return var2 + var3;
    }

    public int get_unsignedsmart_shortorint() {
        return this.array[this.pos] < 0 ? this.get_int() & 2147483647 : this.get_unsignedshort();
    }

    public int get_unsignedsmart_shortorint_null() {
        if (this.array[this.pos] < 0) {
            return this.get_int() & 2147483647;
        } else {
            int var2 = this.get_unsignedshort();
            return 32767 == var2 ? -1 : var2;
        }
    }

    public int get_var_int() {
        byte var2 = this.array[++this.pos - 1];
        int var3 = 0;

        while(var2 < 0) {
            var3 = (var3 | var2 & 127) << 7;
            var2 = this.array[++this.pos - 1];
        }

        return var3 | var2;
    }

    public int get_var_size() {
        int var3 = 0;
        int var4 = 0;

        int var2;
        do {
            var2 = this.get_unsignedbyte();
            var3 |= (var2 & 127) << var4;
            var4 += 7;
        } while(var2 > 127);

        return var3;
    }

    public void tiny_encrypt(int[] arg0) {
        int var3 = this.pos / 8;
        this.pos = 0;

        for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = this.get_int();
            int var6 = this.get_int();
            int var7 = 0;
            int var8 = -1640531527;
            int var9 = 32;

            while(var9-- > 0) {
                var5 += var6 + (var6 << 4 ^ var6 >>> 5) ^ var7 + arg0[var7 & 3];
                var7 += var8;
                var6 += (var5 << 4 ^ var5 >>> 5) + var5 ^ var7 + arg0[var7 >>> 11 & 3];
            }

            this.pos -= 8;
            this.put_int(var5);
            this.put_int(var6);
        }

    }

    public void tiny_decrypt(int[] arg0) {
        int var3 = this.pos / 8;
        this.pos = 0;

        for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = this.get_int();
            int var6 = this.get_int();
            int var7 = -957401312;
            int var8 = -1640531527;
            int var9 = 32;

            while(var9-- > 0) {
                var6 -= (var5 << 4 ^ var5 >>> 5) + var5 ^ var7 + arg0[var7 >>> 11 & 3];
                var7 -= var8;
                var5 -= (var6 << 4 ^ var6 >>> 5) + var6 ^ arg0[var7 & 3] + var7;
            }

            this.pos -= 8;
            this.put_int(var5);
            this.put_int(var6);
        }

    }

    public void tiny_encrypt(int[] arg0, int arg1, int arg2) {
        int var5 = this.pos;
        this.pos = arg1;
        int var6 = (arg2 - arg1) / 8;

        for(int var7 = 0; var7 < var6; ++var7) {
            int var8 = this.get_int();
            int var9 = this.get_int();
            int var10 = 0;
            int var11 = -1640531527;
            int var12 = 32;

            while(var12-- > 0) {
                var8 += var9 + (var9 << 4 ^ var9 >>> 5) ^ arg0[var10 & 3] + var10;
                var10 += var11;
                var9 += var8 + (var8 << 4 ^ var8 >>> 5) ^ arg0[var10 >>> 11 & 3] + var10;
            }

            this.pos -= 8;
            this.put_int(var8);
            this.put_int(var9);
        }

        this.pos = var5;
    }

    public void tiny_decrypt(int[] arg0, int arg1, int arg2) {
        int var5 = this.pos;
        this.pos = arg1;
        int var6 = (arg2 - arg1) / 8;

        for(int var7 = 0; var7 < var6; ++var7) {
            int var8 = this.get_int();
            int var9 = this.get_int();
            int var10 = -957401312;
            int var11 = -1640531527;
            int var12 = 32;

            while(var12-- > 0) {
                var9 -= var8 + (var8 << 4 ^ var8 >>> 5) ^ arg0[var10 >>> 11 & 3] + var10;
                var10 -= var11;
                var8 -= var9 + (var9 << 4 ^ var9 >>> 5) ^ arg0[var10 & 3] + var10;
            }

            this.pos -= 8;
            this.put_int(var8);
            this.put_int(var9);
        }

        this.pos = var5;
    }

    public void rsa_encrypt(BigInteger arg0, BigInteger arg1) {
        int var4 = this.pos;
        this.pos = 0;
        byte[] var5 = new byte[var4];
        System.out.println("Length of byte array before get_bytes: " + var4);
        this.get_bytes(var5, 0, var4);
        System.out.println("Length of byte array after get_bytes: " + var5.length);
        BigInteger var6 = new BigInteger(var5);
        BigInteger var7 = var6.modPow(arg0, arg1);
        byte[] var8 = var7.toByteArray();
        this.pos = 0;
        this.put_short(var8.length);
        this.put_bytes(var8, 0, var8.length);
    }

    public static int method4729(byte[] arg0, int arg1, int arg2) {
        int var4 = -1;

        for(int var5 = arg1; var5 < arg2; ++var5) {
            var4 = var4 >>> 8 ^ Packet.CRC32_TABLE[(var4 ^ arg0[var5]) & 255];
        }

        return ~var4;
    }

    public int put_crc(int arg0) {
        int var3 = method4729(this.array, arg0, this.pos);
        this.put_int(var3);
        return var3;
    }

    public boolean verifycrc() {
        this.pos -= 4;
        int var2 = method4729(this.array, 0, this.pos);
        int var3 = this.get_int();
        return var2 == var3;
    }

    /**
     * Put byte added to 128
     */
    public void put_byte_add(int arg0) {
        this.array[++this.pos - 1] = (byte)(arg0 + 128);
    }

    /**
     * Put byte negated
     */
    public void put_byte_neg(int arg0) {
        this.array[++this.pos - 1] = (byte)(0 - arg0);
    }

    /**
     * Put byte subtracted from 128
     */
    public void put_byte_sub(int arg0) {
        this.array[++this.pos - 1] = (byte)(128 - arg0);
    }

    /**
     * Get unsigned add byte
     */
    public int get_unsignedbyte_add() {
        return this.array[++this.pos - 1] - 128 & 255;
    }

    /**
     * Get unsigned negate byte
     */
    public int get_unsignedbyte_neg() { return 0 - this.array[++this.pos - 1] & 255; }

    /**
     * Get unsigned subtract byte
     */
    public int get_unsignedbyte_sub() { return 128 - this.array[++this.pos - 1] & 255; }

    /**
     * Get signed byte that has had 128 added to it
     *
     */
    public byte get_byte_add() {
        return (byte)(this.array[++this.pos - 1] - 128);
    }

    /**
     * Get signed byte that has been negated
     */
    public byte get_byte_neg() {
        return (byte)(0 - this.array[++this.pos - 1]);
    }

    /**
     * Get signed byte that has been subtracted from 128
     */
    public byte get_byte_sub() {
        return (byte)(128 - this.array[++this.pos - 1]);
    }

    /**
     * Put little endian short
     */
    public void put_short_le(int arg0) {
        this.array[++this.pos - 1] = (byte)arg0;
        this.array[++this.pos - 1] = (byte)(arg0 >> 8);
    }

    /**
     * Put add short
     */
    public void put_short_add(int arg0) {
        this.array[++this.pos - 1] = (byte)(arg0 >> 8);
        this.array[++this.pos - 1] = (byte)(arg0 + 128);
    }

    /**
     * Put little endian add short
     */
    public void put_short_le_add(int arg0) {
        this.array[++this.pos - 1] = (byte)(128 + arg0);
        this.array[++this.pos - 1] = (byte)(arg0 >> 8);
    }

    /**
     * Get unsigned little endian short
     */
    public int get_unsignedshort_le() {
        this.pos += 2;
        return (this.array[this.pos - 2] & 255) + ((this.array[this.pos - 1] & 255) << 8);
    }

    /**
     * Get unsigned add short
     */
    public int get_unsignedshort_add() {
        this.pos += 2;
        return ((this.array[this.pos - 2] & 255) << 8) + (this.array[this.pos - 1] - 128 & 255);
    }

    /**
     * Get unsigned little endian add short
     */
    public int get_unsignedshort_le_add() {
        this.pos += 2;
        return (this.array[this.pos - 2] - 128 & 255) + ((this.array[this.pos - 1] & 255) << 8);
    }

    /**
     * Get signed add short
     */
    public int get_short_add() {
        this.pos += 2;
        int var2 = (this.array[this.pos - 1] - 128 & 255) + ((this.array[this.pos - 2] & 255) << 8);
        if (var2 > 32767) {
            var2 -= 65536;
        }

        return var2;
    }

    /**
     * Get signed little endian add short
     */
    public int get_short_le_add() {
        this.pos += 2;
        int var2 = ((this.array[this.pos - 1] & 255) << 8) + (this.array[this.pos - 2] - 128 & 255);
        if (var2 > 32767) {
            var2 -= 65536;
        }

        return var2;
    }

    /**
     * Put inverse middle endian medium
     */
    public void put_mediumint_ime(int val) {
        this.array[++this.pos - 1] = (byte)(val >> 8);
        this.array[++this.pos - 1] = (byte)(val >> 16);
        this.array[++this.pos - 1] = (byte)val;
    }

    /**
     * Get unsigned little endian medium
     */
    public int get_unsignedmediumint_le() {
        this.pos += 3;
        return ((this.array[this.pos - 2] & 255) << 8) + ((this.array[this.pos - 1] & 255) << 16) + (this.array[this.pos - 3] & 255);
    }

    /**
     * Get unsigned inverse middle endian medium
     */
    public int get_unsignedmediumint_ime() {
        this.pos += 3;
        return ((this.array[this.pos - 3] & 255) << 8) + ((this.array[this.pos - 2] & 255) << 16) + (this.array[this.pos - 1] & 255);
    }

    /**
     * Get signed middle endian medium
     */
    public int get_mediumint_me() {
        this.pos += 3;
        int var2 = ((this.array[this.pos - 3] & 255) << 16) + ((this.array[this.pos - 1] & 255) << 8) + (this.array[this.pos - 2] & 255);
        if (var2 > 8388607) {
            var2 -= 16777216;
        }

        return var2;
    }

    /**
     * Put little endian int
     */
    public void put_int_le(int arg0) {
        this.array[++this.pos - 1] = (byte)arg0;
        this.array[++this.pos - 1] = (byte)(arg0 >> 8);
        this.array[++this.pos - 1] = (byte)(arg0 >> 16);
        this.array[++this.pos - 1] = (byte)(arg0 >> 24);
    }

    /**
     * Put middle endian int
     */
    public void put_int_me(int arg0) {
        this.array[++this.pos - 1] = (byte)(arg0 >> 8);
        this.array[++this.pos - 1] = (byte)arg0;
        this.array[++this.pos - 1] = (byte)(arg0 >> 24);
        this.array[++this.pos - 1] = (byte)(arg0 >> 16);
    }

    /**
     * Put inverse middle endian int
     */
    public void put_int_ime(int arg0) {
        this.array[++this.pos - 1] = (byte)(arg0 >> 16);
        this.array[++this.pos - 1] = (byte)(arg0 >> 24);
        this.array[++this.pos - 1] = (byte)arg0;
        this.array[++this.pos - 1] = (byte)(arg0 >> 8);
    }

    /**
     * Get little endian int
     */
    public int get_int_le() {
        this.pos += 4;
        return (this.array[this.pos - 4] & 255) + ((this.array[this.pos - 3] & 255) << 8) + ((this.array[this.pos - 1] & 255) << 24) + ((this.array[this.pos - 2] & 255) << 16);
    }

    /**
     * Get middle endian int
     */
    public int get_int_me() {
        this.pos += 4;
        return (this.array[this.pos - 3] & 255) + ((this.array[this.pos - 4] & 255) << 8) + ((this.array[this.pos - 1] & 255) << 16) + ((this.array[this.pos - 2] & 255) << 24);
    }

    /**
     * Get inverse middle endian int
     */
    public int get_int_ime() {
        this.pos += 4;
        return (this.array[this.pos - 2] & 255) + ((this.array[this.pos - 1] & 255) << 8) + ((this.array[this.pos - 4] & 255) << 16) + ((this.array[this.pos - 3] & 255) << 24);
    }

    public void get_reversebytes_add(byte[] data, int arg1, int arg2) {
        for(int var5 = arg2 + arg1 - 1; var5 >= arg1; --var5) {
            data[var5] = (byte)(this.array[++this.pos - 1] - 128);
        }
    }
}