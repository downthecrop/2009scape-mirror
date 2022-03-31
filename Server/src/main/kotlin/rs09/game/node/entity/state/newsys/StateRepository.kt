package rs09.game.node.entity.state.newsys

import api.StartupListener
import core.game.node.entity.player.Player
import io.github.classgraph.ClassGraph

class StateRepository : StartupListener{
    override fun startup() {
        loadStateClasses()
    }

    companion object {
        val states = HashMap<String,State>()

        fun loadStateClasses()
        {
            val result = ClassGraph().enableClassInfo().enableAnnotationInfo().acceptPackages("rs09.game.node.entity.state").scan()
            result.getClassesWithAnnotation("rs09.game.node.entity.state.newsys.PlayerState").forEach{
                val key = it.getAnnotationInfo("rs09.game.node.entity.state.newsys.PlayerState").parameterValues[0].value as String
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
}