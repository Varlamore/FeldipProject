package com.cryptic.net.tcp.pow;

public class ProofOfWorkChallengeType {
    public static final ProofOfWorkChallengeType SHA256 = new ProofOfWorkChallengeType(0, 0);
    public static int field120;
    public final int field119;
    public final int id;

    ProofOfWorkChallengeType(int arg0, int arg1) {
        this.id = arg0;
        this.field119 = arg1;
    }

    public int serial_id() {
        return this.field119;
    }
}