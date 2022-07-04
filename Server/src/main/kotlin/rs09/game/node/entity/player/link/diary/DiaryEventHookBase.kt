package rs09.game.node.entity.player.link.diary

import api.LoginListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType

abstract class DiaryEventHookBase : LoginListener {
    protected companion object {
        fun finishTask(entity: Player, diary: DiaryType, level: DiaryLevel, task: Int) {
            val levelName = level.name.lowercase()
            val levelIndex = diary.levelNames.indexOf(levelName)

            if (levelIndex < 0) {
                throw IllegalArgumentException("'$levelName' was not found in diary '$diary'.")
            }

            entity.achievementDiaryManager.finishTask(entity, diary, levelIndex, task)
        }

        fun taskCompleted(entity: Player, diary: DiaryType, level: DiaryLevel, task: Int): Boolean {
            val levelName = level.name.lowercase()
            val levelIndex = diary.levelNames.indexOf(levelName)

            if (levelIndex < 0) {
                throw IllegalArgumentException("'$levelName' was not found in diary '$diary'.")
            }

            return entity.achievementDiaryManager.hasCompletedTask(diary, levelIndex, task)
        }
    }
}