package org.crandor.game.content.dialogue;

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
    OSRS_HAPPY(588), OSRS_NORMAL(594), OSRS_SAD(596), OSRS_LAUGH1(605), OSRS_LAUGH2(606), OSRS_LAUGH3(607), OSRS_LAUGH4(608), //TODO: More
    NORMAL(9760), ANGRY(9792), GRUMPY(9784), ANNOYED(9812), SNEAKY(595), SAD(9776), DISTRESSED(9820), HAPPY(9851), NEARLY_CRYING(9836), CHILD_QUESTIONABLE(7171), CHILD_BACK_AND_FORTH(7172), CHILD_NORMAL(7173), CHILD_SLOW_NOD(7174), CHILD_CRAZY_LAUGH(7175), CHILD_THINKING(7176), CHILD_SAD(7177), CHILD_BIG_EYES(7178), CHILD_LOOKING_OUT(7179),

    NO_EXPRESSION(9760), SAD_TWO(9768), NO_EXPRESSION_TWO(9772), WHY(9776),
    SCARED(9780), MIDLY_ANGRY(9784), VERY_ANGRY(9792), ANGRY_TWO(9796),
    JUST_LISTEN(9804), CALM_TALKING(9808), LOOK_DOWN(9812), WHAT_THE(9816), WHAT_THE_TWO(9820),
    CROOKED_HEAD(9828), GLANCE_DOWN(9832), UNSURE(9836), LISTEN_LAUGH(9840),
    TALK_SWING(9844), GOOFY_LAUGH(9851), DRUNK(9835), ASKING(9827), SUSPICIOUS(9831);


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