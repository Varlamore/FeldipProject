package com.cryptic.net.tcp.pow;

import com.cryptic.io.Buffer;
import com.cryptic.net.tcp.packet.Packet;

public class ProofOfWorkProblem {
    final int difficulty;
    final int field69;
    final String field70;
    public static int loginblock_proofofwork_size;
    ProofOfWorkProblem(int arg0, int difficulty, String arg2) {
        this.field69 = arg0;
        this.difficulty = difficulty;
        this.field70 = arg2;
    }

    ProofOfWorkProblem(Packet arg0) {
        this(arg0.get_unsignedbyte(), arg0.get_unsignedbyte(), arg0.get_string());
    }

    String get_message() {
        return Integer.toHexString(this.field69) + Integer.toHexString(this.difficulty) + this.field70;
    }

    int get_difficulty() {
        return this.difficulty;
    }
}