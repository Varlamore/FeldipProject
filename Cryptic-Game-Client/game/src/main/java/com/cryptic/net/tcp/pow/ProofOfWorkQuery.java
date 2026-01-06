package com.cryptic.net.tcp.pow;

import com.cryptic.io.Buffer;
import com.cryptic.net.tcp.packet.Packet;

import java.util.concurrent.Callable;

public class ProofOfWorkQuery implements Callable {
    public static boolean ItemDefinition_inMembersWorld;
    public static int selectedItemSlot;
    final ProofOfWorkChallenge challenge;
    // $FF: synthetic field
    final ProofOfWorkRequester this$0;
    final Packet buffer;

    ProofOfWorkQuery(ProofOfWorkRequester arg0, Packet arg1, ProofOfWorkChallenge arg2) {
        this.this$0 = arg0;
        this.buffer = arg1;
        this.challenge = arg2;
    }

    public Object call() {
        return this.challenge.start_challenge(this.buffer);
    }
}