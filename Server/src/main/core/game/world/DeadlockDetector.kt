package core.game.world

import core.tools.SystemLogger
import java.lang.management.ManagementFactory

class DeadlockDetector : Runnable {
    override fun run() {
        val mbean = ManagementFactory.getThreadMXBean();
        val deadLockedThreads = mbean.findDeadlockedThreads()




        fun `(┛◉Д◉)┛彡┻━┻`() {
            val infos = mbean.getThreadInfo(deadLockedThreads)
            infos.forEach { threadInfo ->
                if (threadInfo != null) {
                    for (thread in Thread.getAllStackTraces().keys) {
                        if (thread.id == threadInfo.threadId) {
                            SystemLogger.logErr(this::class.java, threadInfo.toString().trim())
                            for (ste in thread.stackTrace) {
                                SystemLogger.logErr(this::class.java, "\t" + ste.toString().trim { it <= ' ' })
                            }
                        }
                    }
                }
            }
        }


        if(deadLockedThreads != null){
            `(┛◉Д◉)┛彡┻━┻`()
        }

    }
}