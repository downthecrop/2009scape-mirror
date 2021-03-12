package rs09.game.world

import rs09.game.system.SystemLogger
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
                            SystemLogger.logErr(threadInfo.toString().trim())
                            for (ste in thread.stackTrace) {
                                SystemLogger.logErr("\t" + ste.toString().trim { it <= ' ' })
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