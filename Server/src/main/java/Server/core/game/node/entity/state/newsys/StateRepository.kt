package core.game.node.entity.state.newsys

import core.game.node.entity.player.Player
import io.github.classgraph.ClassGraph

object StateRepository {
    val states = HashMap<String,State>()

    @JvmStatic
    fun init() {
        val result = ClassGraph().enableClassInfo().enableAnnotationInfo().acceptPackages("core.game.node.entity.state").scan()
        result.getClassesWithAnnotation("core.game.node.entity.state.newsys.PlayerState").forEach{
            val key = it.getAnnotationInfo("core.game.node.entity.state.newsys.PlayerState").parameterValues[0].value as String
            val clazz = it.loadClass().newInstance()
            if(clazz is State) {
                states.put(key, clazz)
            }
        }
    }

    @JvmStatic
    fun forKey(key: String, player: Player): State?{
        val state = states[key]
        if(player.states[key] != null){
            return null
        }
        if(state != null){
            val clazz = state.newInstance(player)
            player.states[key] = clazz
            return clazz
        }
        return null
    }
}