package core.game.node.entity.state

import core.api.StartupListener
import core.game.node.entity.player.Player
import io.github.classgraph.ClassGraph

class StateRepository : StartupListener{
    override fun startup() {
        loadStateClasses()
    }

    companion object {
        val states = HashMap<String, State>()

        fun loadStateClasses()
        {
            val result = ClassGraph().enableClassInfo().enableAnnotationInfo().acceptPackages("content").scan()
            result.getClassesWithAnnotation("core.game.node.entity.state.PlayerState").forEach{
                val key = it.getAnnotationInfo("core.game.node.entity.state.PlayerState").parameterValues[0].value as String
                val clazz = it.loadClass().newInstance()
                if(clazz is State) {
                    states.put(key, clazz)
                }
            }
        }

        @JvmStatic
        fun forKey(key: String, player: Player): State?{
            val state = states[key]
            if(player.hasActiveState(key)){
                return states[key]
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
