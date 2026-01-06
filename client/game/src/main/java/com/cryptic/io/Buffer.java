package com.cryptic.io;

import com.cryptic.Client;
import com.cryptic.ClientConstants;
import com.cryptic.cache.graphics.font.CP1252UTF8;
import com.cryptic.collection.ObjectNode;
import com.cryptic.collection.node.DualNode;
import com.cryptic.collection.node.IntegerNode;
import com.cryptic.collection.node.Node;
import com.cryptic.collection.table.IterableNodeHashTable;
import com.cryptic.net.tcp.packet.PacketPool;
import com.cryptic.net.tcp.isaac.IsaacCipher;
import net.runelite.rs.api.RSBuffer;

import java.math.BigInteger;

public final class Buffer extends DualNode implements RSBuffer {

    private static final int[] BIT_MASKS = {0, 1, 3, 7, 15, 31, 63, 127, 255,
            511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff,
            0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
            0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
            0x7fffffff, -1};

    public static final BigInteger RSA_MODULUS = new BigInteger("131409501542646890473421187351592645202876910715283031445708554322032707707649791604685616593680318619733794036379235220188001221437267862925531863675607742394687835827374685954437825783807190283337943749605737918856262761566146702087468587898515768996741636870321689974105378482179138088453912399137944888201");
    public static final BigInteger RSA_EXPONENT = new BigInteger("65537");

    private static final char[] CHARACTERS = new char[]
            {
                    '\u20ac', '\u0000', '\u201a', '\u0192', '\u201e', '\u2026',
                    '\u2020', '\u2021', '\u02c6', '\u2030', '\u0160', '\u2039',
                    '\u0152', '\u0000', '\u017d', '\u0000', '\u0000', '\u2018',
                    '\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014',
                    '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\u0000',
                    '\u017e', '\u0178'
            };
    public IsaacCipher cipher;

    public byte[] payload;
    public int pos;
    public int bitPosition;

    public int get_remainder_bits(int arg0) {
        return 8 * arg0 - this.bitPosition;
    }

    public void put_byte_secure(int arg0) {
        payload[++this.pos - 1] = (byte) (arg0 + this.cipher.next_int());
    }

    public void release() {
        if (this.payload != null) {
            PacketPool.release(this.payload);
        }
        this.payload = null;
    }

    public Buffer(int size) {
        this.payload = allocate(size);
        this.pos = 0;
    }

    public String get_string() {//gjstr
        int var2 = this.pos;

        while (this.payload[++this.pos - 1] != 0) {
        }

        int var3 = this.pos - var2 - 1;
        return 0 == var3 ? "" : CP1252UTF8.cp1252_to_utf8(this.payload, var2, var3);
    }

    public void set_seed(int[] seed) {
        this.cipher = new IsaacCipher(seed);
    }

    public void set_cipher(IsaacCipher arg0) {
        this.cipher = arg0;
    }

    public void put_byte(int arg0) {
        this.payload[++this.pos - 1] = (byte) arg0;
    }

    public void put_short(int arg0) {
        this.payload[++this.pos - 1] = (byte) (arg0 >> 8);
        this.payload[++this.pos - 1] = (byte) arg0;
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

    public static int allocatedMinCount = 0;

    public static int allocatedMidCount = 0;

    public static int allocatedMaxCount = 0;

    public static final byte[][] allocatedMin = new byte[1000][];

    public static final byte[][] allocatedMid = new byte[250][];

    public static final byte[][] allocatedMax = new byte[50][];

    public static synchronized byte[] allocate(int length) {
        byte[] data;
        if (length == 100 && allocatedMinCount > 0) {
            data = allocatedMin[--allocatedMinCount];
            allocatedMin[allocatedMinCount] = null;
            return data;
        } else if (length == 5000 && allocatedMidCount > 0) {
            data = allocatedMid[--allocatedMidCount];
            allocatedMid[allocatedMidCount] = null;
            return data;
        } else if (length == 30000 && allocatedMaxCount > 0) {
            data = allocatedMax[--allocatedMaxCount];
            allocatedMax[allocatedMaxCount] = null;
            return data;
        } else {
            return new byte[length];
        }
    }

    public void writeCESU8(CharSequence charSequence) {
        int value = method868(charSequence);
        this.payload[++this.pos - 1] = 0;
        this.writeVarInt(value);
        this.pos += method3810(this.payload, this.pos, charSequence);
    }

    public void writeSmartByteShort(int var1) {
        if (var1 >= 0 && var1 < 128) {
            this.writeByte(var1);
        } else if (var1 >= 0 && var1 < 32768) {
            this.writeShort(var1 + 32768);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void writeVarInt(int var1) {
        if ((var1 & -128) != 0) { // L: 228
            if ((var1 & -16384) != 0) { // L: 229
                if ((var1 & -2097152) != 0) { // L: 230
                    if ((var1 & -268435456) != 0) { // L: 231
                        this.writeByte(var1 >>> 28 | 128);
                    }
                    this.writeByte(var1 >>> 21 | 128); // L: 232
                }
                this.writeByte(var1 >>> 14 | 128); // L: 234
            }
            this.writeByte(var1 >>> 7 | 128); // L: 236
        }
        this.writeByte(var1 & 127); // L: 238
    } // L: 239

    public int readNullableLargeSmart() {
        if (this.payload[this.pos] < 0) {
            return this.readInt() & Integer.MAX_VALUE;
        } else {
            int var1 = this.readUShort();
            return var1 == 32767 ? -1 : var1;
        }
    }

    public int readShortSmartSub() {
        int var1 = this.payload[this.pos] & 255;
        return var1 < 128 ? this.readUnsignedByte() - 1 : this.readUShort() - '老';
    }

    public static IterableNodeHashTable readStringIntParameters(Buffer var0, IterableNodeHashTable var1) {
        int var2 = var0.readUnsignedByte();
        int var3;
        if (var1 == null) {
            var3 = method8302(var2);
            var1 = new IterableNodeHashTable(var3);
        }

        for (var3 = 0; var3 < var2; ++var3) {
            boolean var4 = var0.readUnsignedByte() == 1;
            int var5 = var0.readMedium();
            Node var6;
            if (var4) {
                var6 = new ObjectNode(var0.readStringCp1252NullTerminated());
            } else {
                var6 = new IntegerNode(var0.readInt());
            }

            var1.put(var6, (long) var5);
        }

        return var1;
    }

    public static int method8302(int var0) {
        --var0;
        var0 |= var0 >>> 1;
        var0 |= var0 >>> 2;
        var0 |= var0 >>> 4;
        var0 |= var0 >>> 8;
        var0 |= var0 >>> 16;
        return var0 + 1;
    }

    public static int method3810(byte[] output, int offset, CharSequence input) {
        int length = input.length();
        int outIndex = offset;

        for (int currentChar = 0; currentChar < length; ++currentChar) {
            char charAt = input.charAt(currentChar);
            if (charAt <= 127) {
                output[outIndex++] = (byte) charAt;
            } else if (charAt <= 2047) {
                output[outIndex++] = (byte) (192 | charAt >> 6);
                output[outIndex++] = (byte) (128 | charAt & 63);
            } else {
                output[outIndex++] = (byte) (224 | charAt >> 12);
                output[outIndex++] = (byte) (128 | charAt >> 6 & 63);
                output[outIndex++] = (byte) (128 | charAt & 63);
            }
        }

        return outIndex - offset;
    }

    public static int method868(CharSequence str) {
        int length = str.length();
        int byteLength = 0;

        for (int currentChar = 0; currentChar < length; ++currentChar) {
            char charAt = str.charAt(currentChar);
            if (charAt <= 127) {
                ++byteLength;
            } else if (charAt <= 2047) {
                byteLength += 2;
            } else {
                byteLength += 3;
            }
        }

        return byteLength;
    }


    public int readVarInt() {
        byte var1 = this.payload[++this.pos - 1]; // L: 410
        int var2;
        for (var2 = 0; var1 < 0; var1 = this.payload[++this.pos - 1]) { // L:
            // 411
            // 412
            // 414
            var2 = (var2 | var1 & 127) << 7; // L: 413
        }
        return var2 | var1; // L: 416
    }

    public String readCESU8() {
        byte var1 = this.payload[++this.pos - 1];
        if (var1 != 0) {
            throw new IllegalStateException("");
        } else {
            int var2 = this.readVarInt();
            if (var2 + this.pos > this.payload.length) {
                throw new IllegalStateException("");
            } else {
                byte[] var4 = this.payload;
                int var5 = this.pos;
                char[] var6 = new char[var2];
                int var7 = 0;
                int var8 = var5;

                int var11;
                for (int var9 = var2 + var5; var8 < var9; var6[var7++] = (char) var11) {
                    int var10 = var4[var8++] & 255;
                    if (var10 < 128) {
                        if (var10 == 0) {
                            var11 = 65533;
                        } else {
                            var11 = var10;
                        }
                    } else if (var10 < 192) {
                        var11 = 65533;
                    } else if (var10 < 224) {
                        if (var8 < var9 && (var4[var8] & 192) == 128) {
                            var11 = (var10 & 31) << 6 | var4[var8++] & 63;
                            if (var11 < 128) {
                                var11 = 65533;
                            }
                        } else {
                            var11 = 65533;
                        }
                    } else if (var10 < 240) {
                        if (var8 + 1 < var9 && (var4[var8] & 192) == 128 && (var4[var8 + 1] & 192) == 128) {
                            var11 = (var10 & 15) << 12 | (var4[var8++] & 63) << 6 | var4[var8++] & 63;
                            if (var11 < 2048) {
                                var11 = 65533;
                            }
                        } else {
                            var11 = 65533;
                        }
                    } else if (var10 < 248) {
                        if (var8 + 2 < var9 && (var4[var8] & 192) == 128 && (var4[var8 + 1] & 192) == 128 && (var4[var8 + 2] & 192) == 128) {
                            var11 = (var10 & 7) << 18 | (var4[var8++] & 63) << 12 | (var4[var8++] & 63) << 6 | var4[var8++] & 63;
                            if (var11 >= 65536 && var11 <= 1114111) {
                                var11 = 65533;
                            } else {
                                var11 = 65533;
                            }
                        } else {
                            var11 = 65533;
                        }
                    } else {
                        var11 = 65533;
                    }
                }

                String var3 = new String(var6, 0, var7);
                this.pos += var2;
                return var3;
            }
        }
    }

    public Buffer(byte[] payload) {
        this.payload = payload;
        pos = 0;
    }

    public int readLargeSmart() {
        return this.payload[this.pos] < 0 ? this.readInt() & Integer.MAX_VALUE : this.readUShort();
    }

    public int readUShort() {
        pos += 2;
        return ((payload[pos - 2] & 0xff) << 8)
                + (payload[pos - 1] & 0xff);
    }

    public void xteaDecrypt(int[] data, int start, int end) {
        int position = this.pos;
        this.pos = start;
        int numBlocks = (end - start) / 8;

        for (int block = 0; block < numBlocks; ++block) {
            int v0 = this.readInt();
            int v1 = this.readInt();
            int sum = -957401312;
            int delta = -1640531527;

            for (int var11 = 32; var11-- > 0; v0 -= v1 + (v1 << 4 ^ v1 >>> 5) ^ sum + data[sum & 3]) {
                v1 -= v0 + (v0 << 4 ^ v0 >>> 5) ^ data[sum >>> 11 & 3] + sum;
                sum -= delta;
            }

            this.pos -= 8;
            this.writeInt(v0);
            this.writeInt(v1);
        }

        this.pos = position;
    }

    public float get_float() {
        return Float.intBitsToFloat(this.get_int());
    }

    public int get_smart_byteorshort() {
        int var2 = this.payload[this.pos] & 255;
        return var2 < 128 ? this.readUnsignedByte() - 64 : this.readUnsignedShort() - 49152;
    }

    public int get_int() {
        this.pos += 4;
        return ((this.payload[this.pos - 2] & 255) << 8) + ((this.payload[this.pos - 4] & 255) << 24) + ((this.payload[this.pos - 3] & 255) << 16) + (this.payload[this.pos - 1] & 255);
    }

    public int get_short() {
        this.pos += 2;
        int var2 = ((this.payload[this.pos - 2] & 255) << 8) + (this.payload[this.pos - 1] & 255);
        if (var2 > 32767) {
            var2 -= 65536;
        }

        return var2;
    }

    public int g1() {
        return payload[pos++] & 0xff;
    }

    public int get_unsignedmediumint_le() {
        this.pos += 3;
        return ((this.payload[this.pos - 2] & 255) << 8) + ((this.payload[this.pos - 1] & 255) << 16) + (this.payload[this.pos - 3] & 255);
    }

    public int get_unsignedmediumint() {
        this.pos += 3;
        return ((this.payload[this.pos - 3] & 255) << 16) + ((this.payload[this.pos - 2] & 255) << 8) + (this.payload[this.pos - 1] & 255);
    }

    public static Buffer create(int size, IsaacCipher cipher) {
        Buffer stream_1 = new Buffer(new byte[size]);
        stream_1.payload = new byte[size];
        stream_1.cipher = cipher;
        return stream_1;
    }

    public int readSmart() {
        int value = payload[pos] & 0xff;
        if (value < 128)
            return readUnsignedByte() - 64;
        else
            return readUnsignedShort() - 49152;
    }

    public int readUnsignedIntSmartShortCompat() {
        int var1 = 0;

        int var2;
        for (var2 = this.readUShortSmart(); var2 == 32767; var2 = this.readUShortSmart()) {
            var1 += 32767;
        }

        var1 += var2;
        return var1;
    }

    public int get_unsignedsmart_byteorshort_increments() {
        int var2 = 0;

        int var3;
        for (var3 = this.readUShortSmart(); var3 == 32767; var3 = this.readUShortSmart()) {
            var2 += 32767;
        }

        return var2 + var3;
    }

    public int getUIncrementalSmart() {
        int value = 0, remainder;
        for (remainder = readUSmart(); remainder == 32767; remainder = readUSmart()) {
            value += 32767;
        }
        value += remainder;
        return value;
    }


    public int readUSmart() {
        int peek = this.payload[pos] & 0xFF;
        return peek < 128 ? this.readUnsignedByte() : this.readUnsignedShort() - 32768;
    }

    public int readUShortSmart() {
        int var2 = this.payload[this.pos] & 255;
        return var2 < 128 ? this.readUnsignedByte() : this.readUnsignedShort() - 32768;
    }

    public int get_unsignedsmart_byteorshort() {
        int var2 = this.payload[this.pos] & 255;
        return var2 < 128 ? this.get_unsignedbyte() : this.get_unsignedshort() - 32768;
    }

    public int get_unsignedbyte() {
        return this.payload[++this.pos - 1] & 255;
    }

    public int get_unsignedshort() {
        this.pos += 2;
        return (this.payload[this.pos - 1] & 255) + ((this.payload[this.pos - 2] & 255) << 8);
    }


    public int method2529() {
        int var1 = 0;

        int var2;
        for (var2 = this.readUShortSmart(); var2 == 32767; var2 = this.readUShortSmart()) {
            var1 += 32767;
        }

        var1 += var2;
        return var1;
    }

    public int readUTriByte() {
        pos += 3;
        return (0xff & payload[pos - 3] << 16) + (0xff & payload[pos - 2] << 8) + (0xff & payload[pos - 1]);
    }

    public int readUTriByte(int i) {
        pos += 3;
        return (0xff & payload[pos - 3] << 16) + (0xff & payload[pos - 2] << 8) + (0xff & payload[pos - 1]);
    }

    public int readUSmart2() {
        int baseVal = 0;
        int lastVal = 0;
        while ((lastVal = readUSmart()) == 32767) {
            baseVal += 32767;
        }
        return baseVal + lastVal;
    }

    public String readNewString() {
        int i = pos;
        while (payload[pos++] != 0)
            ;
        return new String(payload, i, pos - i - 1);
    }

    public void writeOpcode(int i) {
        payload[pos++] = (byte) (i + cipher.value());
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }

    @Override
    public int getOffset() {
        return pos;
    }

    @Override
    public void setOffset(int offset) {
        this.pos = offset;
    }

    public void writeByte(int value) {
        payload[pos++] = (byte) value;
    }

    public void writeLengthInt(int var1) {
        if (var1 < 0) {
            throw new IllegalArgumentException();
        } else {
            this.payload[this.pos - var1 - 4] = (byte) (var1 >> 24);
            this.payload[this.pos - var1 - 3] = (byte) (var1 >> 16);
            this.payload[this.pos - var1 - 2] = (byte) (var1 >> 8);
            this.payload[this.pos - var1 - 1] = (byte) var1;
        }
    }

    public int writeWordBigEndian(int value) {
        payload[pos++] = (byte) value;
        return (byte) value;
    }

    public void writeShort(int value) {
        payload[pos++] = (byte) (value >> 8);
        payload[pos++] = (byte) value;
    }

    @Override
    public void writeMedium(int var1) {
        this.payload[++this.pos - 1] = (byte) (var1 >> 16);
        this.payload[++this.pos - 1] = (byte) (var1 >> 8);
        this.payload[++this.pos - 1] = (byte) var1;
    }

    public void writeInt(int value) {
        payload[pos++] = (byte) (value >> 24);
        payload[pos++] = (byte) (value >> 16);
        payload[pos++] = (byte) (value >> 8);
        payload[pos++] = (byte) value;
    }

    public void writeLEInt(int value) {
        payload[pos++] = (byte) value;
        payload[pos++] = (byte) (value >> 8);
        payload[pos++] = (byte) (value >> 16);
        payload[pos++] = (byte) (value >> 24);
    }

    public void writeLong(long value) {
        try {
            payload[pos++] = (byte) (int) (value >> 56);
            payload[pos++] = (byte) (int) (value >> 48);
            payload[pos++] = (byte) (int) (value >> 40);
            payload[pos++] = (byte) (int) (value >> 32);
            payload[pos++] = (byte) (int) (value >> 24);
            payload[pos++] = (byte) (int) (value >> 16);
            payload[pos++] = (byte) (int) (value >> 8);
            payload[pos++] = (byte) (int) value;
        } catch (RuntimeException runtimeexception) {
            System.out.println("14395, " + 5 + ", " + value + ", " + runtimeexception);
            throw new RuntimeException();
        }
    }

    @Override
    public void writeStringCp1252NullTerminated(String string) {

    }

    public void writeString(String text) {
        if (text == null) {
            text = "";
        }
        System.arraycopy(text.getBytes(), 0, payload, pos,
                text.length());
        pos += text.length();
        payload[pos++] = 10;
    }

    public void writeBytes(byte[] data, int offset, int length) {
        for (int index = length; index < length + offset; index++)
            payload[pos++] = data[index];
    }

    public void writeBytes(byte[] data) {
        for (byte b : data) {
            writeByte(b);
        }
    }

    public void writeByteAtPosition(int value) {
        payload[pos - value - 1] = (byte) value;
    }

    public int method440() {
        pos += 4;
        return ((payload[pos - 3] & 0xFF) << 24) + ((payload[pos - 4] & 0xFF) << 16) + ((payload[pos - 1] & 0xFF) << 8) + (payload[pos - 2] & 0xFF); //Used to be [- 2] now changed to [pos - 2] may not be correct but this is unused anyway
    }

    public int read24BitInt() {
        return (this.readUnsignedByte() << 16) + (this.readUnsignedByte() << 8) + this.readUnsignedByte();
    }

    public int readShort2() {
        pos += 2;
        int i = ((payload[pos - 2] & 0xff) << 8) + (payload[pos - 1] & 0xff);
        if (i > 32767)
            i -= 65537;
        return i;
    }

    public int readShortSmart() {
        int var1 = this.payload[this.pos] & 255;
        return var1 < 128 ? this.readUnsignedByte() - 64 : this.readUnsignedShort() - '쀀';
    }

    public byte readSignedByte() {
        return payload[pos++];
    }

    public byte get_byte_neg() {
        return (byte)(0 - this.payload[++this.pos - 1]);
    }

    public byte get_byte() {
        return this.payload[++this.pos - 1];
    }
    public int get_unsignedbyte_sub() { return 128 - this.payload[++this.pos - 1] & 255; }

    public byte readByte() {
        return this.payload[++this.pos - 1];
    }

    public int readUnsignedShort() {
        this.pos += 2;
        return (this.payload[this.pos - 1] & 255) + ((this.payload[this.pos - 2] & 255) << 8);
    }

    public int get_unsignedshort_le() {
        this.pos += 2;
        return (this.payload[this.pos - 2] & 255) + ((this.payload[this.pos - 1] & 255) << 8);
    }

    public int get_unsignedshort_add() {
        this.pos += 2;
        return ((this.payload[this.pos - 2] & 255) << 8) + (this.payload[this.pos - 1] - 128 & 255);
    }

    public int get_unsignedbyte_neg() {
        return 0 - this.payload[++this.pos - 1] & 255;
    }

    public int get_unsignedbyte_add() {
        return this.payload[++this.pos - 1] - 128 & 255;
    }

    public void set_byte_mode() {
        this.pos = (7 + this.bitPosition) / 8;
    }

    public void set_bit_mode() {
        this.bitPosition = this.pos * 8;
    }


    public int readSignedShort() {
        this.pos += 2;
        int value = ((this.payload[this.pos - 2] & 0xff) << 8) + (this.payload[this.pos - 1] & 0xff);
        if (value > 32767)
            value -= 0x10000;

        return value;
    }

    public int readShort() {
        this.pos += 2;
        int value = ((this.payload[this.pos - 2] & 0xff) << 8) + (this.payload[this.pos - 1] & 0xff);

        if (value > 60000)
            value = -65535 + value;
        return value;
    }

    public int readTriByte() {
        pos += 3;
        return ((payload[pos - 3] & 0xff) << 16)
                + ((payload[pos - 2] & 0xff) << 8)
                + (payload[pos - 1] & 0xff);
    }

    public int readInt() {
        this.pos += 4;
        return ((this.payload[this.pos - 3] & 255) << 16) + (this.payload[this.pos - 1] & 255) + ((this.payload[this.pos - 2] & 255) << 8) + ((this.payload[this.pos - 4] & 255) << 24);
    }

    public int read24Int() {
        pos += 3;
        return ((payload[pos - 3] & 0xff) << 16) + ((payload[pos - 2] & 0xff) << 8) + (payload[pos - 1] & 0xff);
    }

    public int readMedium() {
        this.pos += 3;
        return ((this.payload[this.pos - 3] & 255) << 16) + (this.payload[this.pos - 1] & 255) + ((this.payload[this.pos - 2] & 255) << 8);
    }

    public long readLong() {
        long msi = (long) readInt() & 0xffffffffL;
        long lsi = (long) readInt() & 0xffffffffL;
        return (msi << 32) + lsi;
    }

    public String readString() {
        int index = pos;
        while (payload[pos++] != 10) ;
        return new String(payload, index, pos - index - 1);
    }


    public final char[] cp1252AsciiExtension = new char[]{
            '€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000',
            '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'
    };

    public String readStringCp1252NullTerminated() {
        int var1 = this.pos;

        while (this.payload[++this.pos - 1] != 0) {
        }

        int var2 = this.pos - var1 - 1;
        return var2 == 0 ? "" : decodeStringCp1252(this.payload, var1, var2);
    }

    public String decodeStringCp1252(byte[] var0, int var1, int var2) {
        char[] var3 = new char[var2];
        int var4 = 0;

        for (int var5 = 0; var5 < var2; ++var5) {
            int var6 = var0[var5 + var1] & 255;
            if (var6 != 0) {
                if (var6 >= 128 && var6 < 160) {
                    char var7 = cp1252AsciiExtension[var6 - 128];
                    if (var7 == 0) {
                        var7 = '?';
                    }

                    var6 = var7;
                }

                var3[var4++] = (char) var6;
            }
        }

        return new String(var3, 0, var4);
    }

    public String readStringOSRS() {
        StringBuilder sb = new StringBuilder();

        for (; ; ) {
            int ch = this.readUnsignedByte();

            if (ch == 0) {
                break;
            }

            if (ch >= 128 && ch < 160) {
                char var7 = CHARACTERS[ch - 128];
                if (0 == var7) {
                    var7 = '?';
                }

                ch = var7;
            }

            sb.append((char) ch);
        }
        return sb.toString();
    }

    public String readJagexString() {
        int i = pos;
        while (payload[pos++] != 10)
            ;
        return new String(payload, i, pos - i - 1);
    }

    public String readNullString() {
        StringBuilder sb = new StringBuilder();

        for (; ; ) {
            int ch = this.readUnsignedByte();

            if (ch == 0) {
                break;
            }

            if (ch >= 128 && ch < 160) {
                char var7 = CHARACTERS[ch - 128];
                if (0 == var7) {
                    var7 = '?';
                }

                ch = var7;
            }

            sb.append((char) ch);
        }
        return sb.toString();
    }

    public byte[] readBytes() {
        int index = pos;
        while (payload[pos++] != 10)
            ;
        byte[] data = new byte[pos - index - 1];
        System.arraycopy(payload, index, data, 0, pos - 1 - index);
        return data;
    }

    public int readUnsignedShortSmartMinusOne() {
        int peek = this.pos & 0xFF;
        return peek < 128 ? this.readUnsignedByte() - 1 : this.readUnsignedShort() - 0x8001;
    }

    public int readBigSmart2() {
        if (pos < 0) {
            return readInt() & Integer.MAX_VALUE; // and off sign bit
        }
        int value = readUnsignedShort();
        return value == 32767 ? -1 : value;
    }

    public int get_unsignedsmart_shortorint_null() {
        if (this.payload[this.pos] < 0) {
            return this.readInt() & 2147483647;
        } else {
            int var2 = this.readUnsignedShort();
            return 32767 == var2 ? -1 : var2;
        }
    }

    public int readNextIntFromPayload() {
        pos += 4;
        return ((payload[pos - 1] & 0xff) << 24) + ((payload[pos - 2] & 0xff) << 16) + ((payload[pos - 3] & 0xff) << 8)
                + (payload[pos - 4] & 0xff);
    }

    public void readBytes(byte[] var1, int var2, int var3) {
        for (int var4 = var2; var4 < var3 + var2; ++var4) {
            var1[var4] = this.payload[++this.pos - 1];
        }
    }

    public void readBytes(final int length, final int offset, final byte[] data) {
        for (int index = offset; index < offset + length; index++) {
            data[index] = payload[pos++];
        }
    }

    public void initBitAccess() {
        bitPosition = pos * 8;
    }

    public int readBits(int amount) {
        int byteOffset = bitPosition >> 3;
        int bitOffset = 8 - (bitPosition & 7);
        int value = 0;
        bitPosition += amount;
        for (; amount > bitOffset; bitOffset = 8) {
            value += (payload[byteOffset++] & BIT_MASKS[bitOffset]) << amount
                    - bitOffset;
            amount -= bitOffset;
        }
        if (amount == bitOffset)
            value += payload[byteOffset] & BIT_MASKS[bitOffset];
        else
            value += payload[byteOffset] >> bitOffset - amount
                    & BIT_MASKS[amount];
        return value;
    }

    public void disableBitAccess() {
        pos = (bitPosition + 7) / 8;
    }

    public int readSignedSmart() {
        int value = this.payload[this.pos] & 0xff;
        if (value < 128)
            return this.readUnsignedByte() - 64;
        else
            return this.readUnsignedShort() - 49152;
    }

    public int getSmart() {
        try {
            // checks current without modifying position
            if (pos >= payload.length) {
                return payload[payload.length - 1] & 0xFF;
            }
            int value = payload[pos] & 0xFF;

            if (value < 128) {
                return readUnsignedByte();
            } else {
                return readUnsignedShort() - 32768;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Client.addReportToServer(e.getMessage());
            return readUnsignedShort() - 32768;
        }
    }

    public void encodeRSA(BigInteger exponent, BigInteger modulus) {
        int length = pos;
        pos = 0;
        byte[] buffer = new byte[length];
        readBytes(length, 0, buffer);

        byte[] rsa = buffer;

        if (ClientConstants.ENABLE_RSA) {
            rsa = new BigInteger(buffer).modPow(exponent, modulus)
                    .toByteArray();
        }

        pos = 0;
        writeByte(rsa.length);
        writeBytes(rsa, rsa.length, 0);
    }

    public void writeNegatedByte(int value) {
        payload[pos++] = (byte) (-value);
    }

    public void writeByteS(int value) {
        payload[pos++] = (byte) (128 - value);
    }

    public int readUByteA() {
        return payload[pos++] - 128 & 0xff;
    }

    public int readNegUByte() {
        return -payload[pos++] & 0xff;
    }

    public boolean readBoolean() {
        return (this.readUnsignedByte() & 1) == 1;
    }

    public int readUnsignedByte() {
        return payload[pos++] & 0xff;
    }

    public int readUByteS() {
        return 128 - payload[pos++] & 0xff;
    }

    public byte readNegByte() {
        return (byte) -payload[pos++];
    }

    public byte readByteS() {
        return (byte) (128 - payload[pos++]);
    }

    public void writeLEShort(int value) {
        payload[pos++] = (byte) value;
        payload[pos++] = (byte) (value >> 8);
    }

    public void writeShortA(int value) {
        payload[pos++] = (byte) (value >> 8);
        payload[pos++] = (byte) (value + 128);
    }

    public void writeLEShortA(int value) {
        payload[pos++] = (byte) (value + 128);
        payload[pos++] = (byte) (value >> 8);
    }

    public int readLEUShort() {
        pos += 2;
        return ((payload[pos - 1] & 0xff) << 8) + (payload[pos - 2] & 0xff);
    }

    public int readUShortA() {
        pos += 2;
        return ((payload[pos - 2] & 0xff) << 8)
                + (payload[pos - 1] - 128 & 0xff);
    }

    public int readUnsignedShortLE() {
        this.pos += 2;
        return ((this.payload[this.pos - 1] & 255) << 8) + (this.payload[this.pos - 2] & 255);
    }

    public int readLEUShortA() {
        pos += 2;
        return ((payload[pos - 1] & 0xff) << 8) + (payload[pos - 2] - 128 & 0xff);
    }

    public int readUnsignedByteAdd() {
        return this.payload[++this.pos - 1] - 128 & 255;
    }

    public int readIntME() {
        this.pos += 4;
        return ((this.payload[this.pos - 2] & 255) << 24) + ((this.payload[this.pos - 4] & 255) << 8) + (this.payload[this.pos - 3] & 255) + ((this.payload[this.pos - 1] & 255) << 16);
    }

    public int readSignedShortAdd() {
        this.pos += 2;
        int var1 = (this.payload[this.pos - 1] - 128 & 255) + ((this.payload[this.pos - 2] & 255) << 8);
        if (var1 > 32767) {
            var1 -= 65536;
        }

        return var1;
    }

    public int readLEShort() {
        pos += 2;
        int value = ((payload[pos - 1] & 0xff) << 8) + (payload[pos - 2] & 0xff);

        if (value > 32767) {
            value -= 0x10000;
        }
        return value;
    }

    public int readLEShortA() {
        pos += 2;
        int value = ((payload[pos - 1] & 0xff) << 8) + (payload[pos - 2] - 128 & 0xff);
        if (value > 32767)
            value -= 0x10000;
        return value;
    }

    public int readIntLittleEndian() {
        pos += 4;
        return ((payload[pos - 4] & 0xFF) << 24) + ((payload[pos - 3] & 0xFF) << 16) + ((payload[pos - 2] & 0xFF) << 8) + (payload[pos - 1] & 0xFF);
    }

    public int readMEInt() { // V1
        pos += 4;
        return ((payload[pos - 2] & 0xff) << 24)
                + ((payload[pos - 1] & 0xff) << 16)
                + ((payload[pos - 4] & 0xff) << 8)
                + (payload[pos - 3] & 0xff);
    }

    public int readIMEInt() {
        pos += 4;
        return ((payload[pos - 3] & 0xff) << 24)
                + ((payload[pos - 4] & 0xff) << 16)
                + ((payload[pos - 1] & 0xff) << 8)
                + (payload[pos - 2] & 0xff);
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public void writeReverseDataA(byte[] data, int length, int offset) {
        for (int index = (length + offset) - 1; index >= length; index--) {
            payload[pos++] = (byte) (data[index] + 128);
        }
    }

    public void readReverseData(byte[] data, int offset, int length) {
        for (int index = (length + offset) - 1; index >= length; index--) {
            data[index] = payload[pos++];
        }
    }

    public int readSmartByteorshort() {
        int value = payload[pos] & 0xFF;
        if (value < 128) {
            return readUnsignedByte() - 0x40;
        } else {
            return readUShort() - 0xc000;
        }
    }


    public void getBytes(int len, int off, byte[] dest) {
        for (int i = off; i < off + len; i++) {
            dest[i] = payload[pos++];
        }
    }

    public int readShortOSRS() {
        this.pos += 2;
        int var1 = (this.payload[this.pos - 1] & 255) + ((this.payload[this.pos - 2] & 255) << 8);
        if (var1 > 32767) {
            var1 -= 65536;
        }

        return var1;
    }

    public void resetPosition() {
        pos = 0;
    }

    public void encryptRSAContent() {
        /* Cache the current position for future use */
        int cachedPosition = pos;

        /* Reset the position */
        pos = 0;

        /* An empty byte array with a capacity of {@code #currentPosition} bytes */
        byte[] decodeBuffer = new byte[cachedPosition];

        /*
         * Gets bytes up to the current position from the buffer and populates
         * the {@code #decodeBuffer}
         */
        getBytes(cachedPosition, 0, decodeBuffer);

        /*
         * The decoded big integer which translates the {@code #decodeBuffer}
         * into a {@link BigInteger}
         */
        BigInteger decodedBigInteger = new BigInteger(decodeBuffer);

        /*
         * This is going to be a mouthful... the encoded {@link BigInteger} is
         * responsible of returning a value which is the value of {@code
         * #decodedBigInteger}^{@link #RSA_EXPONENT} mod (Modular arithmetic can
         * be handled mathematically by introducing a congruence relation on the
         * integers that is compatible with the operations of the ring of
         * integers: addition, subtraction, and multiplication. For a positive
         * integer n, two integers a and b are said to be congruent modulo n)
         * {@link #RSA_MODULES}
         */
        BigInteger encodedBigInteger = decodedBigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);

        /*
         * Returns the value of the {@code #encodedBigInteger} translated to a
         * byte array in big-endian byte-order
         */
        byte[] encodedBuffer = encodedBigInteger.toByteArray();

        /* Reset the position so we can write fresh to the buffer */
        pos = 0;

        /*
         * We put the length of the {@code #encodedBuffer} to the buffer as a
         * standard byte. (Ignore the naming, that really writes a byte...)
         */
        writeByte(encodedBuffer.length);

        /* Put the bytes of the {@code #encodedBuffer} into the buffer. */
        writeBytes(encodedBuffer, encodedBuffer.length, 0);
    }
}