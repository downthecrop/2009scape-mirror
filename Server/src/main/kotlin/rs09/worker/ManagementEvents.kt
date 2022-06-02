package rs09.worker

import com.google.protobuf.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque

/**
 * Processes management-related events e.g clan messages, etc.
 */
object ManagementEvents {
    private var isRunning: Boolean = true
    private val eventQueue: BlockingDeque<Message> = LinkedBlockingDeque()

    val job = GlobalScope.launch {
        while (isRunning) {
            val event = withContext(Dispatchers.IO) { eventQueue.take() }
            handleEvent(event)
        }
    }

    @JvmStatic fun publish(event: Message) {
        eventQueue.offer(event)
    }

    private fun handleEvent(event: Message) {
        TODO("Not yet implemented")
    }
}