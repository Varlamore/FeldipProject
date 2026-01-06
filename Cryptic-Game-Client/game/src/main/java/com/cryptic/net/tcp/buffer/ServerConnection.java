package com.cryptic.net.tcp.buffer;

import com.cryptic.net.tcp.packet.Packet;
import com.cryptic.net.tcp.isaac.IsaacCipher;
import com.cryptic.net.tcp.packet.PacketBit;
import com.cryptic.net.tcp.TcpConnectionMessage;
import com.cryptic.net.tcp.util.Deque;

import java.io.IOException;

public class ServerConnection {
    public boolean waiting_for_prot = true;
    Deque queued_messages = new Deque();
    BufferedConnection socket;
    public PacketBit buffer = new PacketBit(40000);
    Packet out_buffer = new Packet(5000);
    int queued_bytes_count = 0;
    public int idle_counter = 0;
    public int prot_size = 0;
    public int pendingWrites = 0;
    public IsaacCipher isaacCipher;

    public static byte charToByteCp1252(char arg0) {
        byte var2;
        if (arg0 > 0 && arg0 < 128 || arg0 >= 160 && arg0 <= 255) {
            var2 = (byte)arg0;
        } else if (arg0 == 8364) {
            var2 = -128;
        } else if (8218 == arg0) {
            var2 = -126;
        } else if (402 == arg0) {
            var2 = -125;
        } else if (8222 == arg0) {
            var2 = -124;
        } else if (8230 == arg0) {
            var2 = -123;
        } else if (8224 == arg0) {
            var2 = -122;
        } else if (arg0 == 8225) {
            var2 = -121;
        } else if (arg0 == 710) {
            var2 = -120;
        } else if (arg0 == 8240) {
            var2 = -119;
        } else if (arg0 == 352) {
            var2 = -118;
        } else if (arg0 == 8249) {
            var2 = -117;
        } else if (arg0 == 338) {
            var2 = -116;
        } else if (381 == arg0) {
            var2 = -114;
        } else if (arg0 == 8216) {
            var2 = -111;
        } else if (8217 == arg0) {
            var2 = -110;
        } else if (8220 == arg0) {
            var2 = -109;
        } else if (arg0 == 8221) {
            var2 = -108;
        } else if (arg0 == 8226) {
            var2 = -107;
        } else if (arg0 == 8211) {
            var2 = -106;
        } else if (arg0 == 8212) {
            var2 = -105;
        } else if (732 == arg0) {
            var2 = -104;
        } else if (arg0 == 8482) {
            var2 = -103;
        } else if (arg0 == 353) {
            var2 = -102;
        } else if (8250 == arg0) {
            var2 = -101;
        } else if (339 == arg0) {
            var2 = -100;
        } else if (arg0 == 382) {
            var2 = -98;
        } else if (376 == arg0) {
            var2 = -97;
        } else {
            var2 = 63;
        }

        return var2;
    }

    public ServerConnection() {
    }

    public final void clear_queue() {
        this.queued_messages.clear();
        this.queued_bytes_count = 0;
    }

    public final void flush() throws IOException {
        if (null != this.socket && this.queued_bytes_count > 0) {
            this.out_buffer.pos = 0;

            while(true) {
                TcpConnectionMessage var2 = (TcpConnectionMessage)this.queued_messages.last();
                if (null == var2 || var2.size > this.out_buffer.array.length - this.out_buffer.pos) {
                    this.socket.write(this.out_buffer.array, 0, this.out_buffer.pos);
                    this.pendingWrites = 0;
                    break;
                }

                this.out_buffer.put_bytes(var2.packet.array, 0, var2.size);
                this.queued_bytes_count -= var2.size;
                var2.unlink();
                var2.packet.release();
                var2.release();
            }
        }
    }

    public final void addNode(TcpConnectionMessage arg0) {
        this.queued_messages.push_front(arg0);
        arg0.size = arg0.packet.pos;
        arg0.packet.pos = 0;
        this.queued_bytes_count += arg0.size;
    }

    public void set_socket(BufferedConnection arg0) {
        this.socket = arg0;
    }

    public void close() {
        if (null != this.socket) {
            this.socket.close();
            this.socket = null;
        }
    }

    public void removeSocket() {
        this.socket = null;
    }

    public BufferedConnection getSocket() {
        return this.socket;
    }
}
