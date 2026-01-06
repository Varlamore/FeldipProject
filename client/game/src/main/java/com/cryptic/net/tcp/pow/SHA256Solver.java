package com.cryptic.net.tcp.pow;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Solver implements ProofOfWorkSolver {
    final MessageDigest messageDigest = this.create_message_digest();

    SHA256Solver(ProofOfWorkProblem arg0) {
    }

    boolean hash_matches_difficulty(int complexity, String message, long nonce) {
        byte[] digest = this.get_sha256_hash(message, nonce);
        return check_complexity(digest) >= complexity;
    }

    static int check_complexity(byte[] digest) {
        int acc = 0;

        for(int i = 0; i < digest.length; ++i) {
            byte b = digest[i];
            int var5 = method67(b);
            acc += var5;
            if (var5 != 8) {
                break;
            }
        }

        return acc;
    }

    static int method67(byte arg0) {
        int var1 = 0;
        if (arg0 == 0) {
            var1 = 8;
        } else {
            for(int var2 = arg0 & 0xFF; (var2 & 0x80) == 0; var2 <<= 1) {
                ++var1;
            }
        }

        return var1;
    }

    byte[] get_sha256_hash(String message, long nonce) {
        StringBuilder text = new StringBuilder();
        text.append(message).append(Long.toHexString(nonce));
        this.messageDigest.reset();

        try {
            this.messageDigest.update(text.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

        return this.messageDigest.digest();
    }

    MessageDigest create_message_digest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            return null;
        }
    }
}
