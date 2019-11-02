package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.ame.AntiMacroEvent;
import org.crandor.game.content.ame.AntiMacroHandler;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.combat.DeathTask;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.Rights;
import org.crandor.game.node.entity.player.link.RunScript;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.repository.Repository;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the rotten potato plugin.
 * @author 'Vexia
 */
@InitializablePlugin
public final class RottenPotatoPlugin extends OptionHandler {

	/**
	 * Represents the sliced potato options.
	 */
	private static final String[] OPTIONS = new String[] { "rs hd", "heal", "extra", "commands", "drop" };

	/**
	 * Represents the rotten potato item id.
	 */
	private static final int ROTTEN_POTATO = 5733;

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (String option : OPTIONS) {
			ItemDefinition.forId(ROTTEN_POTATO).getConfigurations().put("option:" + option, this);
		}
		new RottenPotatoDialogue().init();
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (player.getDetails().getRights() != Rights.ADMINISTRATOR || option.equals("drop")) {
			player.getInventory().remove(((Item) node));
			return true;
		}
		player.getDialogueInterpreter().open(RottenPotatoDialogue.ID, option);
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	/**
	 * Represents the rotten potato dialogue.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class RottenPotatoDialogue extends DialoguePlugin {

		/**
		 * Represents the rotten potato dialogue id.
		 */
		public static final int ID = 38129321;

		/**
		 * Constructs a new {@code RottenPotatoDialogue} {@code Object}.
		 */
		public RottenPotatoDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code RottenPotatoDialogue} {@code Object}.
		 * @param player the player.
		 */
		public RottenPotatoDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new RottenPotatoDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			if (player.getDetails().getRights() != Rights.ADMINISTRATOR) {
				return true;
			}
			switch ((String) args[0]) {
			case "rs hd":
				interpreter.sendOptions("Op1", "Set all stats", "Wipe inventory.", "Setup POH", "Teleport to player", "Spawn aggressive NPC.");
				stage = 100;
				break;
			case "heal":
			    player.getSettings().setSpecialEnergy(100);
			    player.getSettings().updateRunEnergy(-100);
			    player.getSkills().setLifepointsIncrease(0);
			    player.getSkills().rechargePrayerPoints();
			    player.getSkills().heal(100);
			    player.getSkills().setLevel(Skills.SUMMONING, player.getSkills().getStaticLevel(Skills.SUMMONING));
			    player.getStateManager().remove(EntityState.TELEBLOCK);
			    if (player.getFamiliarManager().hasFamiliar()) {
			    	player.getFamiliarManager().getFamiliar().updateSpecialPoints(-200);
			    }
			    player.sendMessage("All healed!");
				break;
			case "extra":
				interpreter.sendInput(true, "Enter AME name:");
				player.setAttribute("runscript", new RunScript() {
					@Override
					public boolean handle() {
						;
						final AntiMacroEvent event = getEvent((String) getValue());
						if (event == null) {
							player.getPacketDispatch().sendMessage("Unkown event for name - " + (String) getValue());
							return true;
						}
						interpreter.sendInput(true, "options(name)");
						player.setAttribute("runscript", new RunScript() {
							@Override
							public boolean handle() {
								String name = (String) getValue();
								if (name.equals(player.getUsername())) {
									player.getAntiMacroHandler().fireEvent(event.getName());
									return true;
								}
								Player p = Repository.getPlayer(name);
								if (p == null) {
									player.getPacketDispatch().sendMessage("Player unavailable - " + name + ".");
								} else {
									if (!p.getAntiMacroHandler().fireEvent(event.getName())) {
										player.getPacketDispatch().sendMessage("Unable to fire event for " + name + " at this time.");
									}
								}
								return true;
							}

						});
						return true;
					}
				});
				stage = 200;
				break;
			case "commands":
				interpreter.sendOptions("Op4", "Keep me logged in.", "Kick me out.", "Kill me.", "Transmogify me...");
				stage = 400;
				break;
			}
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 100:// eat options.
				end();
				switch (buttonId) {
				case 1:
					setSkills(player);
					break;
				case 2:
					emptyInventory(player);
					break;
				case 3:
					player.getPacketDispatch().sendMessage("This command is not added yet.");
					break;
				case 4:
					teleportToPlayer(player);
					break;
				case 5:
					spawnAggresiveNpc(player);
					break;
				}
				break;
			case 200:// slice
				end();
				switch (buttonId) {
				case 1:
					end();
					break;
				case 2:
					break;
				}
				break;
			case 300:// peel
				switch (buttonId) {
				case 1:
					interpreter.sendOptions("Op3", "Open bank.", "Set PIN to 2468", "Wipe bank.");
					stage = 310;
					break;
				case 2:
					end();
					sendAmes(player);
					break;
				}
				break;
			case 310:
				end();
				switch (buttonId) {
				case 1:
					player.getBank().open();
					break;
				case 2:

					break;
				case 3:
					wipeBank(player);
					break;
				}
				break;
			case 400:
				switch (buttonId) {
				case 1:
					end();
					break;
				case 2:
					player.getPacketDispatch().sendLogout();
					break;
				case 3:
					DeathTask.startDeath(player, player);
					end();
					break;
				case 4:
					end();
					transmogify(player);
					break;
				}
				break;
			}
			return true;
		}

		/**
		 * Method used to set the skills of a player.
		 * @param player the player.
		 */
		public static void setSkills(final Player player) {
			player.getDialogueInterpreter().sendInput(true, "Enter skill syntax(all/reset/name level)");
			player.setAttribute("runscript", new RunScript() {
				@Override
				public boolean handle() {
					final String line = ((String) getValue()).replace("_", " ");
					if (line.equals("all") || line.equals("reset")) {
						boolean reset = line.equals("reset");
						for (int i = 0; i < 24; i++) {
							player.getSkills().setLevel(i, reset ? 1 : 99);
							player.getSkills().setStaticLevel(i, reset ? 1 : 99);
						}
					} else {
						String[] tokens = line.split(" ");
						if (tokens == null) {
							player.getPacketDispatch().sendMessage("Invalid syntax! e.g (Enter skill syntax(all/name)");
							return true;
						}
						final int id = Skills.getSkillByName(tokens[0]);
						if (id == -1 || tokens.length < 1) {
							player.getPacketDispatch().sendMessage("Invalid syntax! e.g (Enter skill syntax(all/name level)");
							return true;
						}
						player.getDialogueInterpreter().sendInput(false, "Enter the level:");

						player.setAttribute("runscript", new RunScript() {
							@Override
							public boolean handle() {
								int level = (int) getValue();
								if (level > 99) {
									level = 99;
								} else if (level < 1) {
									level = 1;
								}
								player.getSkills().setLevel(id, level);
								player.getSkills().setStaticLevel(id, level);
								return true;
							}

						});
					}
					player.getSkills().updateCombatLevel();
					return true;
				}
			});
		}

		/**
		 * Method used to send anti macro events to all players around you.
		 * @param player the player.
		 */
		public static void sendAmes(final Player player) {
			AntiMacroEvent event = player.getAntiMacroHandler().getRandomEvent(-1);
			if (event == null) {
				return;
			}
			for (Player p : RegionManager.getLocalPlayers(player, 8)) {
				if (p == null || p == player || p.getDetails().getRights() == Rights.ADMINISTRATOR) {
					continue;
				}
				p.getAntiMacroHandler().fireEvent(event.getName());
			}
		}

		/**
		 * Gets the event.
		 * @param name the name.
		 * @return the event.
		 */
		public AntiMacroEvent getEvent(String name) {
			for (AntiMacroEvent event : AntiMacroHandler.EVENTS.values()) {
				if (event.getName().toLowerCase().equals(name.replace("_", " "))) {
					return event;
				}
			}
			return null;
		}

		/**
		 * Method used to empty an inventory.
		 * @param player the player.
		 */
		public static void emptyInventory(final Player player) {
			for (Item i : player.getInventory().toArray()) {
				if (i == null) {
					continue;
				}
				if (i.getId() != 5733) {
					player.getInventory().remove(i);
				}
			}
			player.getInventory().refresh();
		}

		/**
		 * Method used to wipe a bank.
		 * @param player the player.
		 */
		public static void wipeBank(final Player player) {
			player.getBank().clear();
			player.getPacketDispatch().sendMessage("Bank all wiped.");
		}

		/**
		 * Method used to transmogify yourself into an npc.
		 * @param player the player.
		 */
		public static void transmogify(final Player player) {
			player.getDialogueInterpreter().sendInput(true, "Enter(id/player)");
			player.setAttribute("runscript", new RunScript() {
				@Override
				public boolean handle() {
					if (((String) getValue()).equals("player")) {
						player.getAppearance().transformNPC(-1);
						return true;
					}
					if (getValue() instanceof Integer) {
						player.getAppearance().transformNPC(Integer.parseInt((String) getValue()));
					}
					return true;
				}
			});
		}

		/**
		 * Method used to teleport to a player.
		 * @param player the player.
		 */
		public static void teleportToPlayer(final Player player) {
			player.getDialogueInterpreter().sendInput(true, "Enter player's display name:");
			player.setAttribute("runscript", new RunScript() {
				@Override
				public boolean handle() {
					final Player target = Repository.getPlayer((String) getValue());
					if (target == null || !target.isActive()) {
						player.getPacketDispatch().sendMessage("That player is offline, or has privacy mode enabled.");
						return true;
					}
					player.getProperties().setTeleportLocation(target.getLocation());
					return true;
				}
			});
		}

		/**
		 * Method used to spawn an aggresive npc.
		 * @param player the player.
		 */
		public static void spawnAggresiveNpc(final Player player) {
			final NPC npc = NPC.create(90, player.getLocation());
			npc.setAggressive(true);
			npc.setRespawn(false);
			npc.init();
			npc.sendChat("Go away!");
		}

		@Override
		public int[] getIds() {
			return new int[] { ID };
		}

	}

}
