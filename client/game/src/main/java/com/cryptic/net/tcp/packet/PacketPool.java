package com.cryptic.net.tcp.packet;

import java.util.ArrayList;
import java.util.HashMap;

public class PacketPool {
    public static byte[][][] ByteArrayPool_arrays;
    public static ArrayList field4402 = new ArrayList();
    public static int[] ByteArrayPool_alternativeSizes;
    public static int[] ByteArrayPool_altSizeArrayCounts;
    static byte[][] ByteArrayPool_medium = new byte[250][];
    static byte[][] ByteArrayPool_small = new byte[1000][];
    static byte[][] field4399 = new byte[50][];
    static byte[][] ByteArrayPool_large = new byte[100][];
    static int ByteArrayPool_smallCount = 0;
    static int ByteArrayPool_largeCount = 0;
    static int field4393 = 0;
    static int field4394 = 1000;
    static int field4395 = 250;
    static int field4396 = 100;
    static int ByteArrayPool_mediumCount = 0;
    static int field4401 = 50;

    static {
        method6183();
        new HashMap();
    }

    public PacketPool() throws Throwable {
        throw new Error();
    }

    public static void method6183() {
        field4402.clear();
        field4402.add(100);
        field4402.add(5000);
        field4402.add(10000);
        field4402.add(30000);
    }

    public static synchronized byte[] allocate(int arg0, boolean arg1) {
        byte[] var3;
        if ((100 == arg0 || arg0 < 100 && arg1) && ByteArrayPool_smallCount > 0) {
            var3 = ByteArrayPool_small[--ByteArrayPool_smallCount];
            ByteArrayPool_small[ByteArrayPool_smallCount] = null;
            return var3;
        } else if ((arg0 == 5000 || arg0 < 5000 && arg1) && ByteArrayPool_mediumCount > 0) {
            var3 = ByteArrayPool_medium[--ByteArrayPool_mediumCount];
            ByteArrayPool_medium[ByteArrayPool_mediumCount] = null;
            return var3;
        } else if ((arg0 == 10000 || arg0 < 10000 && arg1) && ByteArrayPool_largeCount > 0) {
            var3 = ByteArrayPool_large[--ByteArrayPool_largeCount];
            ByteArrayPool_large[ByteArrayPool_largeCount] = null;
            return var3;
        } else if ((30000 == arg0 || arg0 < 30000 && arg1) && field4393 > 0) {
            var3 = field4399[--field4393];
            field4399[field4393] = null;
            return var3;
        } else {
            int var5;
            if (null != ByteArrayPool_arrays) {
                for(var5 = 0; var5 < ByteArrayPool_alternativeSizes.length; ++var5) {
                    if ((ByteArrayPool_alternativeSizes[var5] == arg0 || arg0 < ByteArrayPool_alternativeSizes[var5] && arg1) && ByteArrayPool_altSizeArrayCounts[var5] > 0) {
                        byte[] var4 = ByteArrayPool_arrays[var5][--ByteArrayPool_altSizeArrayCounts[var5]];
                        ByteArrayPool_arrays[var5][ByteArrayPool_altSizeArrayCounts[var5]] = null;
                        return var4;
                    }
                }
            }

            if (arg1 && null != ByteArrayPool_alternativeSizes) {
                for(var5 = 0; var5 < ByteArrayPool_alternativeSizes.length; ++var5) {
                    if (arg0 <= ByteArrayPool_alternativeSizes[var5] && ByteArrayPool_altSizeArrayCounts[var5] < ByteArrayPool_arrays[var5].length) {
                        return new byte[ByteArrayPool_alternativeSizes[var5]];
                    }
                }
            }

            return new byte[arg0];
        }
    }

    public static synchronized byte[] allocate(int arg0) {
        return allocate(arg0, false);
    }

    public static synchronized void release(byte[] arg0) {
        if (arg0.length == 100 && ByteArrayPool_smallCount < field4394) {
            ByteArrayPool_small[++ByteArrayPool_smallCount - 1] = arg0;
        } else if (5000 == arg0.length && ByteArrayPool_mediumCount < field4395) {
            ByteArrayPool_medium[++ByteArrayPool_mediumCount - 1] = arg0;
        } else if (arg0.length == 10000 && ByteArrayPool_largeCount < field4396) {
            ByteArrayPool_large[++ByteArrayPool_largeCount - 1] = arg0;
        } else if (30000 == arg0.length && field4393 < field4401) {
            field4399[++field4393 - 1] = arg0;
        } else {
            if (null != ByteArrayPool_arrays) {
                for(int var2 = 0; var2 < ByteArrayPool_alternativeSizes.length; ++var2) {
                    if (arg0.length == ByteArrayPool_alternativeSizes[var2] && ByteArrayPool_altSizeArrayCounts[var2] < ByteArrayPool_arrays[var2].length) {
                        ByteArrayPool_arrays[var2][ByteArrayPool_altSizeArrayCounts[var2]++] = arg0;
                        return;
                    }
                }
            }

        }
    }
}