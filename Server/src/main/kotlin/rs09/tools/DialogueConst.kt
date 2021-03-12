package rs09.tools

const val START_DIALOGUE = 0
const val END_DIALOGUE = 1000000

/**
 * Stage gets set to this by the sendChoices() method if there's more than one option a player can pick when starting a conversation. I.E. multiple simultaneous quests.
 */
const val DIALOGUE_INITIAL_OPTIONS_HANDLE = 1001