package core.game.worldevents.holiday.easter

import core.game.world.map.Location
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class EasterEventTests {
    @Test fun eventShouldOnlyTriggerInApril()
    {
        val inst = EasterEvent()
        val calInst = Calendar.getInstance()

        calInst.set(Calendar.YEAR, 2024)
        calInst.set(Calendar.MONTH, Calendar.APRIL)
        calInst.set(Calendar.DAY_OF_MONTH, 1)
        Assertions.assertEquals(true, inst.checkActive(calInst))

        calInst.set(Calendar.MONTH, Calendar.MARCH)
        Assertions.assertEquals(false, inst.checkActive(calInst))

        calInst.set(Calendar.DAY_OF_MONTH, 31)
        Assertions.assertEquals(false, inst.checkActive(calInst))

        calInst.set(Calendar.MONTH, Calendar.MAY)
        calInst.set(Calendar.DAY_OF_MONTH, 1)
        Assertions.assertEquals(false, inst.checkActive(calInst))
    }

    @Test fun getLocationsForNameShouldReturnExpectedList()
    {
        val expectations = arrayOf(
            Pair(EasterEvent.LOC_LUM, EasterEvent.LUMBRIDGE_SPOTS),
            Pair(EasterEvent.LOC_DRAYNOR, EasterEvent.DRAYNOR_SPOTS),
            Pair(EasterEvent.LOC_TGS, EasterEvent.TREE_GNOME_STRONGHOLD_SPOTS),
            Pair(EasterEvent.LOC_EDGE, EasterEvent.EDGEVILLE_SPOTS),
            Pair(EasterEvent.LOC_FALADOR, EasterEvent.FALADOR_SPOTS),
            Pair("Random Invalid Dummy Data", EasterEvent.LUMBRIDGE_SPOTS)
        )

        for ((locName, locList) in expectations)
        {
            Assertions.assertEquals(locList, EasterEvent.locationsForName(locName))
        }
    }

    @Test fun getRandomLocationDataShouldReturnARandomLocationNameAndListOfUniqueRandomLocations()
    {
        val inst = EasterEvent()

        val sampleA = inst.getRandomLocations()
        var sampleB = inst.getRandomLocations()

        //Assert that they are equal when they contain the same information (just in case JVM is goofy)
        Assertions.assertEquals(sampleA, Pair(sampleA.first, sampleA.second.toTypedArray().toList()))

        //build in some safety against extreme unlikelihood
        var similarityTolerance = 3
        //Check 100 times just to be very sure that we're not getting the same data
        for (i in 0 until 100) {
            if (sampleA == sampleB) similarityTolerance--
            sampleB = inst.getRandomLocations()
        }
        Assertions.assertEquals(true, similarityTolerance > 0)
    }

    @Test fun eachLocationGroupShouldContainNoDuplicateLocations() {
        for (locSet in arrayOf(
            EasterEvent.FALADOR_SPOTS,
            EasterEvent.EDGEVILLE_SPOTS,
            EasterEvent.DRAYNOR_SPOTS,
            EasterEvent.LUMBRIDGE_SPOTS,
            EasterEvent.TREE_GNOME_STRONGHOLD_SPOTS
        ))
        {
            val usedLocations = ArrayList<Location>()
            for (loc in locSet)
            {
                if (!usedLocations.contains(loc))
                    usedLocations.add(loc)
                else
                    Assertions.fail("Loc $loc appeared more than once!")
            }
        }
    }

    @Test fun gfxForEggShouldReturnCorrectGfx()
    {
        val expectations = arrayOf(
            Pair(EasterEvent.EGG_A, EasterEvent.GFX_A),
            Pair(EasterEvent.EGG_B, EasterEvent.GFX_B),
            Pair(EasterEvent.EGG_C, EasterEvent.GFX_C),
            Pair(EasterEvent.EGG_D, EasterEvent.GFX_D),
            Pair(-1, EasterEvent.GFX_A)
        )

        for ((egg, gfx) in expectations)
            Assertions.assertEquals(gfx, EasterEvent.gfxForEgg(egg))
    }
}