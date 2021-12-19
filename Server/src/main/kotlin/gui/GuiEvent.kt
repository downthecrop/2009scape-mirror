package gui

sealed class GuiEvent {
    class UpdatePulseCount(val amount: Int) : GuiEvent()
    class UpdateTickTime(val time: Long) : GuiEvent()
    class UpdateQueuedPackets(val amount: Int) : GuiEvent()
    class AddDebugMessage(val message: String) : GuiEvent()
    class AddDefaultMessage(val message: String) : GuiEvent()
    class AddAIPMessage(val message: String): GuiEvent()
}
