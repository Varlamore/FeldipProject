package com.cryptic.js5.net;

import com.cryptic.collection.node.DualNode;
import com.cryptic.js5.disk.Js5Archive;
import net.runelite.rs.api.RSNetFileRequest;

public class NetFileRequest extends DualNode implements RSNetFileRequest {
    public Js5Archive js5Archive;
    public int crc;
    public byte padding;
}
