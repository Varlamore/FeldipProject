package com.cryptic.net.tcp.pow;

import com.cryptic.io.Buffer;
import com.cryptic.net.tcp.packet.Packet;

public interface ProofOfWorkChallenge {
    static ProofOfWorkChallenge get_challenge(ProofOfWorkChallengeType type) {
        switch(type.id) {
            case 0:
                return new SHA256Challenge();
            default:
                throw new IllegalArgumentException();
        }
    }

    Buffer start_challenge(Packet arg0);
}
