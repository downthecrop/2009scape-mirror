package api

/**
 * Used to generate interface settings hashes.
 * Deprecates [core.game.container.access.BitregisterAssembler]
 * @author Ceikry
 */
class IfaceSettingsBuilder {
    /**
     * Contains the value which should be sent in access mask packet.
     */
    private var value = 0

    /**
     * Sets right click option settings. If specified option is not allowed, it
     * might not appear in the context menu, and if it does, it will throw an error when clicked.
     * @param optionId The option index.
     */
    fun enableOption(optionId: Int): IfaceSettingsBuilder {
        require(!(optionId < 0 || optionId > 9)) { "Option index must be 0-9." }
        value = value or (0x1 shl optionId + 1)
        return this
    }

    fun enableOptions(vararg ids: Int): IfaceSettingsBuilder {
        for (i in ids.indices) {
            enableOption(ids[i])
        }
        return this
    }

    fun enableOptions(ids: IntRange): IfaceSettingsBuilder {
        for (i in ids.start..ids.endInclusive) {
            enableOption(i)
        }
        return this
    }

    fun enableAllOptions(): IfaceSettingsBuilder {
        for (i in 0..9) {
            enableOption(i)
        }
        return this
    }

    fun enableOptions(vararg options: String?): IfaceSettingsBuilder {
        for (i in options.indices) {
            enableOption(i)
        }
        return this
    }

    /**
     * Sets use on option settings. If nothing is allowed then 'use' option will
     * not appear in right click menu.
     */
    fun setUseOnSettings(
        groundItems: Boolean,
        npcs: Boolean,
        objects: Boolean,
        otherPlayer: Boolean,
        selfPlayer: Boolean,
        component: Boolean
    ): IfaceSettingsBuilder {
        var useFlag = 0
        if (groundItems) {
            useFlag = useFlag or 0x1
        }
        if (npcs) {
            useFlag = useFlag or 0x2
        }
        if (objects) {
            useFlag = useFlag or 0x4
        }
        if (otherPlayer) {
            useFlag = useFlag or 0x8
        }
        if (selfPlayer) {
            useFlag = useFlag or 0x10
        }
        if (component) {
            useFlag = useFlag or 0x20
        }
        value = value or (useFlag shl 11)
        return this
    }

    /**
     * Sets interface events depth. For example, we have inventory interface
     * which is opened on gameframe interface (548) If depth is 1, then the
     * clicks in inventory will also invoke click event handler scripts on
     * gameframe interface. Setting depth to 2 also allows dragged items to
     * leave the bounds of their container, useful for things such as bank tabs.
     * @param depth The depth value.
     */
    fun setInterfaceEventsDepth(depth: Int): IfaceSettingsBuilder {
        require(!(depth < 0 || depth > 7)) { "depth must be 0-7." }
        value = value and (0x7 shl 18).inv()
        value = value or (depth shl 18)
        return this
    }

    /**
     * Allows items in this interface container to be switched around
     * @return this builder
     */
    fun enableSlotSwitch(): IfaceSettingsBuilder {
        value = value or (1 shl 21)
        return this
    }

    /**
     * Allows items in this interface container to have a "Use" option
     * @return this builder
     */
    fun enableUseOption(): IfaceSettingsBuilder {
        value = value or (1 shl 17)
        return this
    }

    fun enableExamine(): IfaceSettingsBuilder {
        value = value or (1 shl 9)
        return this
    }

    /**
     * Allows this component to have items used on it
     * @return this builder
     */
    fun enableUseOnSelf(): IfaceSettingsBuilder {
        value = value or (1 shl 22)
        return this
    }

    /**
     * Allows this component to switch item slots with a slot that contains a null (empty slot)
     * @return this builder
     */
    fun enableNullSlotSwitch(): IfaceSettingsBuilder {
        value = value or (1 shl 23)
        return this
    }

    /**
     * Gets the current value.
     * @return The value.
     */
    fun build(): Int {
        return value
    }
}