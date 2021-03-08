package core

import core.game.world.map.Location
import java.io.File

class JSONUtils {
    companion object {


        /**
         * Parses a location from the format "x,y,z"
         * @author Ceikry
         * @param locString The string to parse
         * @return Location
         */
        @JvmStatic
        fun parseLocation(locString: String): Location {
            val locTokens = locString.split(",").map { it.toInt() }
            return Location(locTokens[0], locTokens[1], locTokens[2])
        }

        /**
         * Parses a path string
         * @author Ceikry
         * @param pathString The string to parse
         * @return a String with the proper file separators for the current OS.
         */
        @JvmStatic
        fun parsePath(pathString: String): String {
            var pathTokens: List<String>? = null
            if(pathString.contains("/"))
                pathTokens = pathString.split("/")
            else if(pathString.contains("\\"))
                pathTokens = pathString.split("\\")

            pathTokens ?: return pathString //return the initial pathString if path does not contain file separators.
            var pathProduct = ""
            for(token in pathTokens){
                if(token != "")
                    pathProduct += "$token${File.separator}"
            }

            return pathProduct
        }

    }
}