package core.cache.crypto

/**
 * Represents a ISAAC key pair, for both input and output.
 */
class ISAACPair(
    val input: ISAACCipher,
	val output: ISAACCipher
)
