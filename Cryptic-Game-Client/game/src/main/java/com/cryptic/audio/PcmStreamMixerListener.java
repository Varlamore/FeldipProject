package com.cryptic.audio;

import com.cryptic.collection.node.Node;
import net.runelite.rs.api.RSPcmStreamMixerListener;

public abstract class PcmStreamMixerListener extends Node implements RSPcmStreamMixerListener {
	int field395;
	abstract void remove2();
	
	abstract int update();
}
