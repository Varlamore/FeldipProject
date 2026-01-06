package com.cryptic.net.tcp;

import com.cryptic.entity.model.HashLink;
import com.cryptic.net.tcp.isaac.IsaacCipher;
import com.cryptic.net.tcp.packet.PacketBit;

public class TcpConnectionMessage extends HashLink {
    static TcpConnectionMessage[] pool = new TcpConnectionMessage[300];
    static int pool_cursor = 0;
    int size_raw;
    public PacketBit packet;
    public int size;

    TcpConnectionMessage() {
    }

    public static TcpConnectionMessage get_empty() {
        TcpConnectionMessage var1 = get();
        var1.size_raw = 0;
        var1.packet = new PacketBit(5000);
        return var1;
    }

    static TcpConnectionMessage get() {
        return 0 == pool_cursor ? new TcpConnectionMessage() : pool[--pool_cursor];
    }

    public static TcpConnectionMessage create(IsaacCipher cipher) {
        TcpConnectionMessage packet = get();
       // packet.size_raw = prot.size;
        if (packet.size_raw == -1) {
            packet.packet = new PacketBit(260);
        } else if (packet.size_raw == -2) {
            packet.packet = new PacketBit(10000);
        } else if (packet.size_raw <= 18) {
            packet.packet = new PacketBit(20);
        } else if (packet.size_raw <= 98) {
            packet.packet = new PacketBit(100);
        } else {
            packet.packet = new PacketBit(260);
        }

        packet.packet.set_cipher(cipher);
        packet.size = 0;
        return packet;
    }

    public void release() {
        if (pool_cursor < pool.length) {
            pool[++pool_cursor - 1] = this;
        }
    }
}
