package plugin.ai.general.scriptrepository

import io.github.classgraph.ClassGraph

object PlayerScripts {
    class PlayerScript(val identifier: String, val description: Array<String>, val name: String, val clazz: Class<*>)
    val identifierMap = HashMap<String,PlayerScript>()


    fun init(){
        val result = ClassGraph().enableAnnotationInfo().acceptPackages("plugin.ai.general.scriptrepository").scan()
        result.getClassesWithAnnotation("plugin.ai.general.scriptrepository.PlayerCompatible").forEach { res ->
            val description = res.getAnnotationInfo("plugin.ai.general.scriptrepository.ScriptDescription").parameterValues[0].value as Array<String>
            val identifier = res.getAnnotationInfo("plugin.ai.general.scriptrepository.ScriptIdentifier").parameterValues[0].value.toString()
            val name = res.getAnnotationInfo("plugin.ai.general.scriptrepository.ScriptName").parameterValues[0].value.toString()
            identifierMap[identifier] = PlayerScript(identifier,description,name,res.loadClass())
        }
    }
}