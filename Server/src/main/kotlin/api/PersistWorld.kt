package api

interface PersistWorld : ContentInterface {
    fun save()
    fun parse()
}
