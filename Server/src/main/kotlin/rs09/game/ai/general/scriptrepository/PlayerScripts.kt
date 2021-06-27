package rs09.game.ai.general.scriptrepository

import io.github.classgraph.ClassGraph

object PlayerScripts {
    class PlayerScript(val identifier: String, val description: Array<String>, val name: String, val clazz: Class<*>)
    val identifierMap = HashMap<String,PlayerScript>()


    fun init(){
        val result = ClassGraph().enableAnnotationInfo().acceptPackages("rs09.game.ai.general.scriptrepository").scan()
        result.getClassesWithAnnotation("rs09.game.ai.general.scriptrepository.PlayerCompatible").forEach { res ->
            val description = res.getAnnotationInfo("rs09.game.ai.general.scriptrepository.ScriptDescription").parameterValues[0].value as Array<String>
            val identifier = res.getAnnotationInfo("rs09.game.ai.general.scriptrepository.ScriptIdentifier").parameterValues[0].value.toString()
            val name = res.getAnnotationInfo("rs09.game.ai.general.scriptrepository.ScriptName").parameterValues[0].value.toString()
            identifierMap[identifier] = PlayerScript(identifier,description,name,res.loadClass())
        }
    }
}