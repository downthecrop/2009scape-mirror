package content.global.skill.construction.decoration.pohstorage

import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * This represents the container where all the POH storable items are.
 */

class StorageContainer {

    // item IDs stored in this container
    private val stored: MutableSet<Int> = mutableSetOf()

    // page index for the active interface
    private var currentPage: Int = 0

    // functions to add item, withdraw, check for, etc.
    fun addItem(id: Int) {
        stored.add(id)
    }

    fun withdraw(id: Int): Boolean {
        return stored.remove(id)
    }

    fun contains(id: Int): Boolean = id in stored

    fun getItems(): List<Int> = stored.toList()

    // paging
    fun getPageIndex(): Int = currentPage

    fun nextPage(totalItems: Int, pageSize: Int) {
        val next = currentPage + 1
        if (next * pageSize < totalItems) {
            currentPage = next
        }
    }

    fun prevPage() {
        if (currentPage > 0) {
            currentPage--
        }
    }

    fun resetPage() {
        currentPage = 0
    }

    // saving to json and reading back
    fun toJson(): JSONObject {
        val json = JSONObject()
        val items = JSONArray()
        stored.forEach { items.add(it) }
        json["items"] = items
        return json
    }

    fun readJson(json: JSONObject) {
        stored.clear()
        val items = json["items"] as? JSONArray ?: return
        for (id in items) {
            stored.add((id as Number).toInt())
        }
        currentPage = 0
    }

    // clear storage
    fun clear() {
        stored.clear()
        currentPage = 0
    }
}