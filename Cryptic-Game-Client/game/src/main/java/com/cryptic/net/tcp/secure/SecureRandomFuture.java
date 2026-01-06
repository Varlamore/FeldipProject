package com.cryptic.net.tcp.secure;

import com.cryptic.net.tcp.secure.SecureRandomCallable;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SecureRandomFuture {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future future = this.executor.submit(new SecureRandomCallable());

    public SecureRandomFuture() {
    }

    public void shutdown() {
        this.executor.shutdown();
        this.executor = null;
    }

    public boolean isDone() {
        return this.future.isDone();
    }

    public SecureRandom get() {
        try {
            return (SecureRandom)this.future.get();
        } catch (Exception var3) {
            return SecureRandomCallable.method219();
        }
    }

}
