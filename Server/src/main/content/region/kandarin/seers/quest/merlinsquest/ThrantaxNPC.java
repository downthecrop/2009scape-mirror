package content.region.kandarin.seers.quest.merlinsquest;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.npc.NPC;
import core.game.world.map.Location;

/**
 * Handles the thrantax npc.
 * @author Vexia
 */
public class ThrantaxNPC extends NPC {

        /**
         * The player.
         */
        public Player player;

        /**
         * Constructs a new {@code AnimatedArmour} {@code Object}.
         * @param player The player.
         * @param location The location to spawn.
         */
        protected ThrantaxNPC(Player player, Location location) {
                super(238, location);
                this.player = player;
        }

        @Override
        public boolean isHidden(final Player player) {
                if (player.getQuestRepository().getQuest("Merlin's Crystal").getStage(player) == 80 && this.getAttribute("thrantax_owner", "").equals(player.getUsername())) {
                        return false;
                }
                return true;
        }

        @Override
        public void init() {
                super.init();
        }

        @Override
        public void handleTickActions() {
                if (player != null && !player.isActive() || player != null && !player.getLocation().withinDistance(getLocation(), 20)) {
                        clear();
                }
                super.handleTickActions();
        }

        @Override
        public void finalizeDeath(Entity entity) {
                if (entity instanceof Player) {
                        Player p = entity.asPlayer();
                        super.finalizeDeath(p);
                }
        }

        @Override
        public boolean canAttack(Entity entity) {
                return getAttribute("thrantax_owner", entity) == entity;
        }

        @Override
        public void clear() {
                super.clear();
                player.getHintIconManager().clear();
        }
}
