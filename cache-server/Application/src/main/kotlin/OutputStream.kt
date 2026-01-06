import java.nio.ByteBuffer

class OutputStream(capacity: Int = 10000000) {
    private var buffer = ByteBuffer.allocate(capacity)

    fun writeByte(value: Int) {
        buffer.put(value.toByte())
    }

    fun writeShort(value: Int) {
        buffer.putShort(value.toShort())
    }

    fun writeInt(value: Int) {
        buffer.putInt(value)
    }

    fun writeString(value: String) {
        buffer.put(value.toByteArray())
        buffer.put(0.toByte())
    }

    fun flip(): ByteArray {
        val result = ByteArray(buffer.position())
        buffer.flip()
        buffer.get(result)
        return result
    }
}
