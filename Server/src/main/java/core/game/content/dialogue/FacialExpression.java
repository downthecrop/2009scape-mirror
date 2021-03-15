package core.game.content.dialogue;

/**
 * Represents the facial expressions (the animations the entity does when
 * talking).
 *
 * @author Emperor
 * @author Empathy
 */
public enum FacialExpression {

    /**
     * The normal talking expression.
     */

    //Names based off https://2009scape.wiki/w/Chathead/Animations

    //Chat heads from oldschool 2009scape?
    //Maybe for gnomes or dwarves? Chat heads are frozen when used on Human NPCs
    OLD_HAPPY(588),
    OLD_CALM_TALK1(589),
    OLD_CALM_TALK2(590),
    OLD_DEFAULT(591),
    OLD_EVIL1(592),
    OLD_EVIL2(593),
    OLD_NORMAL(594),
    OLD_SNEAKY(595),
    OLD_DISTRESSED(596),
    OLD_DISTRESSED2(597),
    OLD_ALMOST_CRYING(598),
    OLD_BOWS_HEAD_SAD(599),
    OLD_DRUNK_LEFT(600),
    OLD_DRUNK_RIGHT(601),
    OLD_NOT_INTERESTED(602),
    OLD_SLEEPY(603),
    OLD_PLAIN_EVIL(604),
    OLD_LAUGH1(605),
    OLD_LAUGH2(606),
    OLD_LAUGH3(607),
    OLD_LAUGH4(608),
    OLD_EVIL_LAUGH(609),
    OLD_SAD(610),
    OLD_MORE_SAD(611),
    OLD_ON_ONE_HAND(612),
    OLD_NEARLY_CRYING(613),
    OLD_ANGRY1(614),
    OLD_ANGRY2(615),
    OLD_ANGRY3(616),
    OLD_ANGRY4(617),

    //Chatheads from 2009?
    NOD_YES(9741),
    WORRIED(9743),
    HALF_WORRIED(9745), //Not on the wiki, first half of worried
    AMAZED(9746),
    EXTREMELY_SHOCKED(9750),
    GUILTY(9758),
    HALF_GUILTY(9760), //Not on the wiki, first half of guilty
    SAD(9761),
    CRYING(9765),
    HALF_CRYING(9768), //Not on the wiki, but plays the first half of the crying animation
    AFRAID(9772),
    SCARED(9776),
    PANICKED(9780),
    ANNOYED(9784),
    ANGRY(9785),
    FURIOUS(9792),
    ANGRY_WITH_SMILE(9796), // Not on the wiki
    ANGRY_WITH_SMILE_AND_EVIL_EYE(9798), //Not on the Wiki
    SLEEPING(9802),
    SILENT(9804),
    NEUTRAL(9808),
    THINKING(9812),
    HALF_THINKING(9814),
    DISGUSTED(9816),
    DISGUSTED_HEAD_SHAKE(9823), //Not on the wiki
    ASKING(9827),
    HALF_ASKING(9830), //Not on wiki, first half of Asking
    ROLLING_EYES(9831), //9832, 9833 are the same
    HALF_ROLLING_EYES(9834), //Not on Wiki, first half of Rolling eyes
    DRUNK(9835),
    SUSPICIOUS(9836),
    LAUGH(9840),
    LOUDLY_LAUGHING(9841),
    EVIL_LAUGH(9842),
    FRIENDLY(9844),
    HAPPY(9847),
    JOLLY(9851),
    STRUGGLE(9865), //TODO: More?
    //9855-9857 are like disgusted? does it just repeat after this?

    //Child Chathead?
    CHILD_EVIL_LAUGH(7171),
    CHILD_FRIENDLY(7172),
    CHILD_NORMAL(7173),
    CHILD_NEUTRAL(7174),
    CHILD_LOUDLY_LAUGHING(7175),
    CHILD_THINKING(7176),
    CHILD_SAD(7177),
    CHILD_GUILTY(7178),
    CHILD_SUSPICIOUS(7179); //TODO: More?



    /**
     * The animation id.
     */
    private final int animationId;

    /**
     * Constructs a new {@code FacialExpression} {@code Object}.
     *
     * @param animationId The animation id.
     */
    FacialExpression(int animationId) {
        this.animationId = animationId;
    }

    /**
     * Gets the animation id.
     *
     * @return The animation id.
     */
    public int getAnimationId() {
        return animationId;
    }
}