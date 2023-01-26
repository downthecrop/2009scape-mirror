package core.game.bots

object PlayerScripts {
    class PlayerScript(val identifier: String, val description: Array<String>, val name: String, val clazz: Class<*>)
    val identifierMap = HashMap<String, PlayerScript>()
}