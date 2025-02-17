package core.game.dialogue;

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
    CHILD_ANGRY(7168),
    CHILD_SIDE_EYE(7169),
    CHILD_RECALLING(7170),
    CHILD_EVIL_LAUGH(7171),
    CHILD_FRIENDLY(7172),
    CHILD_NORMAL(7173),
    CHILD_NEUTRAL(7174),
    CHILD_LOUDLY_LAUGHING(7175),
    CHILD_THINKING(7176),
    CHILD_SAD(7177),
    CHILD_GUILTY(7178),
    CHILD_SUSPICIOUS(7179),
    CHILD_SURPRISED(7180),

    ; //TODO: More?

    /*
     * From some sources: here's a potential list of chatheads.
     * // 667
     * Chat animation group: 1540; linked animations: [7, 8, 9, 6824]
     * Chat animation group: 1489; linked animations: [225, 6550, 6551, 6552, 6553, 6555, 8372, 8373, 8374, 8375, 8581, 8582, 9178, 9179, 9180, 9181, 9183, 9187, 9189, 9190, 9192, 9202]
     * Chat animation group: 82; linked animations: [554, 555, 556, 557, 562, 563, 564, 565, 567, 568, 569, 570, 571, 572, 573, 574, 575, 576, 577, 578, 588, 589, 590, 591, 592, 593, 594, 595, 596, 597, 598, 599, 600, 601, 602, 603, 610, 611, 612, 613, 614, 615, 616, 617]
     * Chat animation group: 84; linked animations: [558, 559, 560, 561]
     * Chat animation group: 80; linked animations: [584, 585, 586, 587]
     * Chat animation group: 78; linked animations: [3874]
     * Chat animation group: 77; linked animations: [4119, 4120, 4121, 4122]
     * Chat animation group: 1124; linked animations: [4843, 4844, 4845, 4846, 8388, 8389, 8390, 8391, 8392, 8393, 8394, 8403, 8404, 8405, 8406, 8898, 8899, 8900]
     * Chat animation group: 1309; linked animations: [5661, 5662, 5663, 5665]
     * Chat animation group: 1421; linked animations: [6244, 6245, 6246, 7636, 7637, 7638, 7639, 8380, 8381, 8382, 8383, 8475, 8476, 8477, 8478]
     * Chat animation group: 1627; linked animations: [7168, 7169, 7170, 7171, 7172, 7173, 7176, 7177, 7178, 7179, 7180, 8824]
     * Chat animation group: 1698; linked animations: [7539, 7540, 7541, 7542, 8447, 8448, 8449, 8450]
     * Chat animation group: 1885; linked animations: [8395, 8396, 8397, 8398]
     * Chat animation group: 1882; linked animations: [8411, 8412, 8413, 8414]
     * Chat animation group: 1887; linked animations: [8443, 8444, 8445, 8446, 9315, 9316, 9317, 9318, 9319]
     * Chat animation group: 1906; linked animations: [8579, 8580, 8583, 8584, 8585, 8656, 8657, 8659, 8660, 8661, 8662]
     */

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