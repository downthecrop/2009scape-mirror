package core.game.node.entity.impl;

//facilitating pseudo-entity-queues in a clean manner with minimal disruption to existing code.
//this is NOT perfect.
//bit shit, really.
public enum PulseType {
    STANDARD, //standard pulse slot, should be interrupted/replaced by most things
    STRONG, //enforces itself as the only that can run
    CUSTOM_1, //custom slots for extra tasks that should run alongside standard tasks.
    CUSTOM_2
}
