package content.global.skill.fletching.log

import core.tools.StringUtils

object GrammarHelpers {
    fun makeFriendlyName(name: String): String {
        return name.replace("(u)", "").trim { it <= ' ' }
    }

    /**
     * Returns "a" or "an" depending on whether the word begins with a vowel
     */
    @Suppress("GrazieInspection")
    fun aOrAn(word: String): String {
        return when (StringUtils.isPlusN(word)) {
            true -> "an"
            false -> "a"
        }
    }
}