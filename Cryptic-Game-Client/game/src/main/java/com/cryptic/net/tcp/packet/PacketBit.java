package com.cryptic.net.tcp.packet;

import com.cryptic.net.tcp.isaac.IsaacCipher;

import java.math.BigInteger;

import static com.cryptic.io.Buffer.RSA_EXPONENT;
import static com.cryptic.io.Buffer.RSA_MODULUS;

public class PacketBit extends Packet {
    static final int[] BIT_MASKS = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, 2147483647, -1};
    IsaacCipher isaacCipher;
    int bitpos;

    public PacketBit(int arg0) {
        super(arg0);
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
        get_bytes(decodeBuffer, 0, cachedPosition);

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
        put_byte(encodedBuffer.length);

        /* Put the bytes of the {@code #encodedBuffer} into the buffer. */
        put_bytes(encodedBuffer, encodedBuffer.length, 0);
    }

    public void set_seed(int[] seed) {
        this.isaacCipher = new IsaacCipher(seed);
    }

    public void set_cipher(IsaacCipher arg0) {
        this.isaacCipher = arg0;
    }

    public void put_byte_secure(int arg0) {
        super.array[++super.pos - 1] = (byte)(arg0 + this.isaacCipher.next_int());
    }

    public int get_unsignedbyte_secure() {
        return super.array[++super.pos - 1] - this.isaacCipher.next_int() & 255;
    }

    public boolean has_secure_smart() {
        int var2 = super.array[super.pos] - this.isaacCipher.peek_next_int() & 255;
        return var2 >= 128;
    }

    public int get_smart_byteorshort_secure() {
        int var2 = super.array[++super.pos - 1] - this.isaacCipher.next_int() & 255;
        return var2 < 128 ? var2 : (var2 - 128 << 8) + (super.array[++super.pos - 1] - this.isaacCipher.next_int() & 255);
    }

    public void get_bytes_secure(byte[] arg0, int arg1, int arg2) {
        for(int var5 = 0; var5 < arg2; ++var5) {
            arg0[arg1 + var5] = (byte)(super.array[++super.pos - 1] - this.isaacCipher.next_int());
        }

    }

    public void set_bit_mode() {
        this.bitpos = super.pos * 8;
    }

    public int read_bits(int arg0) {
        int var3 = this.bitpos >> 3;
        int var4 = 8 - (this.bitpos & 7);
        int var5 = 0;
        this.bitpos += arg0;

        while(arg0 > var4) {
            var5 += (super.array[var3++] & BIT_MASKS[var4]) << arg0 - var4;
            arg0 -= var4;
            var4 = 8;
        }

        if (var4 == arg0) {
            var5 += super.array[var3] & BIT_MASKS[var4];
        } else {
            var5 += super.array[var3] >> var4 - arg0 & BIT_MASKS[arg0];
        }

        return var5;
    }

    public void set_byte_mode() {
        super.pos = (7 + this.bitpos) / 8;
    }

    public int get_remainder_bits(int arg0) {
        return 8 * arg0 - this.bitpos;
    }
}