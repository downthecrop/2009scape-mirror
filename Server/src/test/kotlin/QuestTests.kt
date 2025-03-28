import content.data.Quests
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.link.quest.QuestRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class QuestTests {
    lateinit var testPlayer: MockPlayer
    init {
        TestUtils.preTestSetup()
        testPlayer = TestUtils.getMockPlayer("test")
    }

    class TestQuest : Quest(Quests.TEST_QUEST, 0, 0, 1, 1, 0, 1, 2) {
        override fun newInstance(`object`: Any?): Quest {
            return this
        }
    }
    val testQuest = TestQuest()

    @Test fun getIndexShouldNotThrowException() {
        Assertions.assertDoesNotThrow {
            testQuest.index
        }
    }

    @Test fun registerShouldMakeQuestImmediatelyAvailable() {
        QuestRepository.register(testQuest)
        Assertions.assertNotNull(QuestRepository.getQuests()[testQuest.quest])
    }

    @Test fun registerShouldMakeQuestImmediatelyAvailableToInstances() {
        QuestRepository.register(testQuest)
        val instance = QuestRepository(testPlayer)
        Assertions.assertNotNull(instance.getQuest(testQuest.quest))
    }

    @Test fun getStageOnUnstartedQuestShouldNotThrowException() {
        QuestRepository.register(testQuest)
        val instance = QuestRepository(testPlayer)
        Assertions.assertDoesNotThrow {
            instance.getStage(testQuest)
        }
    }

    @Test fun setStageOnUnstartedQuestShouldNotThrowException() {
        QuestRepository.register(testQuest)
        val instance = QuestRepository(testPlayer)
        Assertions.assertDoesNotThrow {
            instance.setStage(testQuest, 10)
        }
    }

    @Test fun completeQuestShouldThrowExceptionIfAlreadyComplete() {
        Assertions.assertThrows(IllegalStateException::class.java, {
            QuestRepository.register(testQuest)
            val repo = QuestRepository(testPlayer)
            repo.getQuest(Quests.TEST_QUEST).finish(testPlayer)
            repo.getQuest(Quests.TEST_QUEST).finish(testPlayer)
        }, "Quest completed twice without throwing an exception or threw wrong exception!")
    }
}