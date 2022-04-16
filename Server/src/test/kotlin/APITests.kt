import api.IfaceSettingsBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

object APITests {
    @Test fun testIfaceSettings(){
        var builder = IfaceSettingsBuilder()
        val testOptions = builder.enableOptions(0..9).build()
        Assertions.assertEquals(2046, testOptions, "Testing option flags")

        builder = IfaceSettingsBuilder()
        val testSlotSwitch = builder.enableSlotSwitch().build()
        Assertions.assertEquals(2097152, testSlotSwitch, "Testing slot switch flag")

        builder = IfaceSettingsBuilder()
        val testNullSlot = builder.enableNullSlotSwitch().build()
        Assertions.assertEquals(8388608, testNullSlot, "Testing null slotswitch flag")

        builder = IfaceSettingsBuilder()
        val testUseWithFlags = builder.setUseOnSettings(true, true, true, true, true, true).build()
        Assertions.assertEquals(129024, testUseWithFlags, "Testing usewith flags")

        builder = IfaceSettingsBuilder()
        val testIfaceDepth = builder.setInterfaceEventsDepth(2).build()
        Assertions.assertEquals(2 shl 18, testIfaceDepth and 1835008, "Testing events depth")

        builder = IfaceSettingsBuilder()
        val testUseOption = builder.enableUseOption().build()
        Assertions.assertEquals(1 shl 17, testUseOption, "Testing use option")

        builder = IfaceSettingsBuilder()
        val testUseOnSelf = builder.enableUseOnSelf().build()
        Assertions.assertEquals(1 shl 22, testUseOnSelf, "Testing use on self flag")

        builder = IfaceSettingsBuilder()
        val testAllOptions = builder
            .enableAllOptions()
            .enableExamine()
            .enableNullSlotSwitch()
            .enableSlotSwitch()
            .enableUseOnSelf()
            .enableUseOption()
            .setInterfaceEventsDepth(2)
            .setUseOnSettings(true, true, true, true, true, true)
            .build()
        Assertions.assertEquals(15466494, testAllOptions, "Testing all options")
    }


}