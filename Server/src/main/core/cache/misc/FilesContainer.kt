package core.cache.misc

data class FilesContainer(
	var version: Int = -1,
	var crc: Int = -1,
	var nameHash: Int = -1,
	var filesIndexes: IntArray? = null,
	var files: Array<Container?>? = null
)