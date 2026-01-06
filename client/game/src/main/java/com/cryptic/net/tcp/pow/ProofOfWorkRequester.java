package com.cryptic.net.tcp.pow;

import com.cryptic.io.Buffer;
import com.cryptic.net.tcp.packet.Packet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProofOfWorkRequester {
    ExecutorService executor_service = Executors.newSingleThreadExecutor();
    Future response;
    final ProofOfWorkChallenge challenge;
    final Packet packet;

    public ProofOfWorkRequester(Packet arg0, ProofOfWorkChallenge arg1) {
        this.packet = arg0;
        this.challenge = arg1;
        this.send_request();
    }

    public boolean has_pow_response() {
        return this.response.isDone();
    }

    public void destroy() {
        this.executor_service.shutdown();
        this.executor_service = null;
    }

    public Packet get_response_buffer() {
        try {
            return (Packet) this.response.get();
        } catch (Exception var3) {
            return null;
        }
    }

    void send_request() {
        this.response = this.executor_service.submit(new ProofOfWorkQuery(this, this.packet, this.challenge));
    }
}