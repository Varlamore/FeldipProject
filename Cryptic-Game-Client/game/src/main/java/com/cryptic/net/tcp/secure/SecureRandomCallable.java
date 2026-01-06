package com.cryptic.net.tcp.secure;

import java.security.SecureRandom;
import java.util.concurrent.Callable;

public class SecureRandomCallable implements Callable {
    SecureRandomCallable() {
    }

    public Object call() {
        return method219();
    }

    public static SecureRandom method219() {
        SecureRandom var1 = new SecureRandom();
        var1.nextInt();
        return var1;
    }

    public static int method1803(int arg0, int arg1) {
        for(int var3 = 0; var3 < 8; ++var3) {
            if (arg1 <= 30 + arg0) {
                return var3;
            }

            int var4 = arg0 + 30;
            arg0 = var4 + (var3 != 1 && 3 != var3 ? 5 : 20);
        }

        return 0;
    }
}
