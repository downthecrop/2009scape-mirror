package core.game.node.entity.player.link;

import core.game.system.task.Pulse;
import core.game.node.entity.Entity;

abstract class TeleportPulse extends Pulse {
    private Entity entity;

    public TeleportPulse(Entity e) { this.entity = e; }

    public abstract boolean pulse();

    @Override
    public void start() {
        entity.setAttribute("tele-pulse", this);
        super.start();
    }

    @Override
    public void stop() {
        entity.removeAttribute("tele-pulse");
        super.stop();
    }
}
