package ms.net.packet

/**
 * Represents the types of packet headers.
 * @author Emperor
 */
enum class PacketHeader {

    /**
     * The normal packet header.
     */
    NORMAL,

    /**
     * The byte packet header.
     */
    BYTE,

    /**
     * The short packet header.
     */
    SHORT
}