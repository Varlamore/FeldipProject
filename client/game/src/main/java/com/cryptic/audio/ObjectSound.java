package com.cryptic.audio;

import com.cryptic.ClientConstants;
import com.cryptic.cache.def.ObjectDefinition;
import com.cryptic.collection.node.Node;
import com.cryptic.collection.node.NodeDeque;
import net.runelite.api.coords.LocalPoint;
import net.runelite.rs.api.RSObjectSound;

public final class ObjectSound extends Node implements RSObjectSound {

	public static NodeDeque objectSounds = new NodeDeque();

	int plane;

	int x;

	int y;

	int maxX;

	int maxY;

	int field812;

	int soundEffectId;

	public RawPcmStream stream1;

	int field819;

	int field811;
	int[] soundEffectIds;

	int field809;

	ObjectDefinition obj;

	public RawPcmStream stream2;

	ObjectSound() {
	} // L: 27

	void set() {
		int var1 = this.soundEffectId; // L: 44
		ObjectDefinition var2 = this.obj.transform(); // L: 45
		if (var2 != null) { // L: 46
			this.soundEffectId = var2.soundId(); // L: 47
			this.field812 = var2.soundRange() * 128; // L: 48
			this.field819 = var2.soundMin(); // L: 49
			this.field811 = var2.soundMax(); // L: 50
			this.soundEffectIds = var2.soundIds(); // L: 51
		} else {
			this.soundEffectId = -1; // L: 54
			this.field812 = 0; // L: 55
			this.field819 = 0; // L: 56
			this.field811 = 0; // L: 57
			this.soundEffectIds = null; // L: 58
		}
		if (var1 != this.soundEffectId && this.stream1 != null) { // L: 60
			StaticSound.pcmStreamMixer.removeSubStream(this.stream1); // L: 61
			this.stream1 = null; // L: 62
		}
	} // L: 64

	public static void addObjectSounds(int var0, int var1, int var2, ObjectDefinition var3, int var4) {
		ObjectSound object = new ObjectSound(); // L: 67
		object.plane = var0; // L: 68
		object.x = var1 * 128; // L: 69
		object.y = var2 * 128; // L: 70
		int var6 = var3.sizeX(); // L: 71
		int var7 = var3.sizeY(); // L: 72
		if (var4 == 1 || var4 == 3) { // L: 73
			var6 = var3.sizeY(); // L: 74
			var7 = var3.sizeX(); // L: 75
		}
		object.maxX = (var6 + var1) * 128; // L: 77
		object.maxY = (var7 + var2) * 128; // L: 78
		object.soundEffectId = var3.soundId(); // L: 79
		object.field812 = var3.soundRange() * 128; // L: 80
		object.field819 = var3.soundMin(); // L: 81
		object.field811 = var3.soundMax(); // L: 82
		object.soundEffectIds = var3.soundIds(); // L: 83
		if (var3.getTransforms() != null) { // L: 84
			object.obj = var3; // L: 85
			object.set(); // L: 86
		}
		objectSounds.addFirst(object); // L: 88
		if (object.soundEffectIds != null) { // L: 89
			object.field809 = object.field819 + (int) (Math.random() * (double) (object.field811 - object.field819));
		}
	} // L: 90

	public static void updateObjectSounds(int var0, int var1, int var2, int var3) {
		for (ObjectSound var4 = (ObjectSound) objectSounds.last(); var4 != null; var4 = (ObjectSound) objectSounds.previous()) { // L:
			// 93
			if (var4.soundEffectId != -1 || var4.soundEffectIds != null) { // L:
				// 94
				int var5 = 0; // L: 95
				if (var1 > var4.maxX) { // L: 96
					var5 += var1 - var4.maxX;
				} else if (var1 < var4.x) { // L: 97
					var5 += var4.x - var1;
				}
				if (var2 > var4.maxY) { // L: 98
					var5 += var2 - var4.maxY;
				} else if (var2 < var4.y) { // L: 99
					var5 += var4.y - var2;
				}
				if (var5 - 64 <= var4.field812 && ClientConstants.areaSoundEffectVolume != 0 && var0 == var4.plane) { // L:
					// 100
					var5 -= 64; // L: 111
					if (var5 < 0) { // L: 112
						var5 = 0;
					}
					int var6 = (var4.field812 - var5) * ClientConstants.areaSoundEffectVolume / var4.field812; // L:
					// 113
					if (var4.stream1 == null) { // L: 114
						if (var4.soundEffectId >= 0) { // L: 115
							SoundEffect var7 = SoundEffect.readSoundEffect(StaticSound.soundEffectsArchive, var4.soundEffectId, 0); // L:
							// 116
							if (var7 != null) { // L: 117
								RawSound var8 = var7.toRawSound().resample(StaticSound.decimator); // L:
								// 118
								RawPcmStream var9 = RawPcmStream.createRawPcmStream(var8, 100, var6); // L:
								// 119
								var9.setNumLoops(-1); // L: 120
								StaticSound.pcmStreamMixer.addSubStreamm(var9); // L:
								// 121
								var4.stream1 = var9; // L: 122
							}
						}
					} else {
						var4.stream1.method790(var6); // L: 126
					}
					if (var4.stream2 == null) { // L: 127
						if (var4.soundEffectIds != null && (var4.field809 -= var3) <= 0) {
							int var11 = (int) (Math.random() * (double) var4.soundEffectIds.length); // L:
							SoundEffect var12 = SoundEffect.readSoundEffect(StaticSound.soundEffectsArchive, var4.soundEffectIds[var11], 0); // L:

							if (var12 != null) { // L: 131
								RawSound var13 = var12.toRawSound().resample(StaticSound.decimator); // L:
								// 132
								RawPcmStream var10 = RawPcmStream.createRawPcmStream(var13, 100, var6); // L:
								// 133
								var10.setNumLoops(0); // L: 134
								StaticSound.pcmStreamMixer.addSubStreamm(var10); // L:
								// 135
								var4.stream2 = var10; // L: 136
								var4.field809 = var4.field819 + (int) (Math.random() * (double) (var4.field811 - var4.field819)); // L:
								// 137
							}
						}
					} else {
						var4.stream2.method790(var6);
						if (!var4.stream2.hasNext()) {
							var4.stream2 = null;
						}
					}
				} else {
					if (var4.stream1 != null) {
						StaticSound.pcmStreamMixer.removeSubStream(var4.stream1);
						var4.stream1 = null;
					}
					if (var4.stream2 != null) {
						StaticSound.pcmStreamMixer.removeSubStream(var4.stream2);
						var4.stream2 = null;
					}
				}
			}
		}
	}

	public static void clearObjectSounds() {
		for (ObjectSound var0 = (ObjectSound) objectSounds.last(); var0 != null; var0 = (ObjectSound) objectSounds.previous()) { // L:
			// 30
			if (var0.stream1 != null) { // L: 31
				StaticSound.pcmStreamMixer.removeSubStream(var0.stream1); // L: 32
				var0.stream1 = null; // L: 33
			}
			if (var0.stream2 != null) { // L: 35
				StaticSound.pcmStreamMixer.removeSubStream(var0.stream2);
				var0.stream2 = null;
			}
		}
		objectSounds.clear(); // L: 40
	} // L: 41

	public static void update() {
		for (ObjectSound var1 = (ObjectSound) ObjectSound.objectSounds.last(); var1 != null; var1 = (ObjectSound) ObjectSound.objectSounds.previous()) {
			if (var1.obj != null) {
				var1.set();
			}
		}
	}

	@Override
	public LocalPoint getMinPosition()
	{
		return new LocalPoint(getX(), getY());
	}

	@Override
	public LocalPoint getMaxPosition()
	{
		return new LocalPoint(getMaxX(), getMaxY());
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getMaxX() {
		return maxX;
	}

	@Override
	public int getMaxY() {
		return maxY;
	}

	@Override
	public int getSoundEffectId() {
		return soundEffectId;
	}

	@Override
	public int getPlane() {
		return plane;
	}
}
