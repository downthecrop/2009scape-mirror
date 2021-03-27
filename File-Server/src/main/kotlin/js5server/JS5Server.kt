package js5server

import com.displee.cache.CacheLibrary
import java.io.File
import kotlin.concurrent.thread

object JS5Server {

    @JvmStatic
    fun main(args: Array<String>) {
        val start = System.currentTimeMillis()
        val file = File("./file-server.properties")
        if (!file.exists()) {
            System.err.println("Unable to find server properties file.")
            return
        }

        println("Start up...")

        var revision = 530
        var subRevision = 1
        var port = 43593
        var threads = 0
        lateinit var cachePath: String
        file.forEachLine { line ->
            val (key, value) = line.split("=")
            when (key) {
                "revision" -> revision = value.toInt()
                "subrevision" -> subRevision = value.toInt()
                "port" -> port = value.toInt()
                "threads" -> threads = value.toInt()
                "cachePath" -> cachePath = value
            }
        }

        println("Loaded configuration.")

        val cache = CacheLibrary(cachePath)
        val versionTable = cache.generateOldUkeys()
        val fileServer = FileServer(DataProvider(cache), versionTable)

        println("Loaded cache revision $revision from $cachePath")

        val network = JS5Net(fileServer, revision, subRevision)

        println("Loading complete [${System.currentTimeMillis() - start}ms] / bound to 127.0.0.1:$port")

        val runtime = Runtime.getRuntime()
        runtime.addShutdownHook(thread(start = false) { network.stop() })
        network.start(port, threads)
    }
}