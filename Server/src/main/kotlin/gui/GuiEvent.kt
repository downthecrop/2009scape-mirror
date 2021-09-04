package gui

sealed class GuiEvent {
    class UpdatePulseCount(val amount: Int) : GuiEvent()
    class UpdateTickTime(val time: Long) : GuiEvent()
}
