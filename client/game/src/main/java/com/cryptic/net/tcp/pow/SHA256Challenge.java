package com.cryptic.net.tcp.pow;

import com.cryptic.io.Buffer;
import com.cryptic.net.tcp.packet.Packet;

public class SHA256Challenge implements ProofOfWorkChallenge {
    public SHA256Challenge() {
    }

    void solve_challenge(Packet in, Buffer out) {
        ProofOfWorkProblem challenge = new ProofOfWorkProblem(in);
        SHA256Solver solver = new SHA256Solver(challenge);

        long nonce;
        for(nonce = 0L; !solver.hash_matches_difficulty(challenge.get_difficulty(), challenge.get_message(), nonce); ++nonce) {
        }

        out.writeLong(nonce);
    }

    @Override
    public Buffer start_challenge(Packet arg0) {
        Buffer out = new Buffer(100);
        this.solve_challenge(arg0, out);
        return out;
    }
}
