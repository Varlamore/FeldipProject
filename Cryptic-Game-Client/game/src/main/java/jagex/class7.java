package jagex;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.runelite.mapping.Export;
import net.runelite.mapping.ObfuscatedName;
import net.runelite.mapping.ObfuscatedSignature;

@ObfuscatedName("ao")
public class class7 {
	@ObfuscatedName("am")
	ExecutorService field10;
	@ObfuscatedName("ap")
	Future field11;
	@ObfuscatedName("af")
	@ObfuscatedSignature(
		descriptor = "Luk;"
	)
	final Buffer field12;
	@ObfuscatedName("aj")
	@ObfuscatedSignature(
		descriptor = "Laj;"
	)
	final class3 field13;

	@ObfuscatedSignature(
		descriptor = "(Luk;Laj;)V"
	)
	public class7(Buffer var1, class3 var2) {
		this.field10 = Executors.newSingleThreadExecutor();
		this.field12 = var1;
		this.field13 = var2;
		this.method50();
	}

	@ObfuscatedName("am")
	@ObfuscatedSignature(
		descriptor = "(I)Z",
		garbageValue = "1275905562"
	)
	public boolean method44() {
		return this.field11.isDone();
	}

	@ObfuscatedName("ap")
	@ObfuscatedSignature(
		descriptor = "(B)V",
		garbageValue = "25"
	)
	public void method45() {
		this.field10.shutdown();
		this.field10 = null;
	}

	@ObfuscatedName("af")
	@ObfuscatedSignature(
		descriptor = "(B)Luk;",
		garbageValue = "1"
	)
	public Buffer method46() {
		try {
			return (Buffer)this.field11.get();
		} catch (Exception var2) {
			return null;
		}
	}

	@ObfuscatedName("aj")
	@ObfuscatedSignature(
		descriptor = "(B)V",
		garbageValue = "57"
	)
	void method50() {
		this.field11 = this.field10.submit(new class1(this, this.field12, this.field13));
	}

}
