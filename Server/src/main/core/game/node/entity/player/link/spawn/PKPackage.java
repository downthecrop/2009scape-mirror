package core.game.node.entity.player.link.spawn;

import core.game.node.item.Item;

/**
 * A pk package.
 *
 * @author Vexia
 */
public enum PKPackage {

    MELEE("Melee Set", 0, 2, new Item(4587), new Item(8850), new Item(10828), new Item(5698), new Item(7462), new Item(1712), new Item(4367), new Item(3105), new Item(1079), new Item(1127), new Item(995, 7000000), new Item(386, 220), new Item(2441, 20), new Item(2437, 20), new Item(2443, 20), new Item(2445, 20), new Item(6686, 10), new Item(3025, 10), new Item(2435, 15), new Item(9075, 320), new Item(560, 600), new Item(555, 3000), new Item(565, 450), new Item(557, 5000)),
    RANGE_TANK("Range Tank Set", 0, 2, new Item(1712), new Item(2503), new Item(1079), new Item(7462), new Item(3105), new Item(9185), new Item(1201), new Item(10499), new Item(1163), new Item(9243, 30), new Item(9244, 14), new Item(9144, 100), new Item(892, 30), new Item(861), new Item(995, 7000000), new Item(386, 220), new Item(9075, 320), new Item(560, 600),  new Item(555, 3000), new Item(565, 450), new Item(557, 5000), new Item(386, 220), new Item(2441, 20), new Item(2437, 20), new Item(2443, 20), new Item(2445, 20), new Item(6686, 10), new Item(3025, 10), new Item(2435, 15)),
    MED_LEVEL("Med Level Set", 0,  1, new Item(1712), new Item(10828), new Item(7462), new Item(3105), new Item(4587), new Item(8850), new Item(1079), new Item(1127), new Item(5698), new Item(4675), new Item(3842), new Item(4111), new Item(4113), new Item(4409), new Item(995, 7000000), new Item(386, 220), new Item(2441, 20), new Item(2437, 20), new Item(2443, 20), new Item(2445, 20), new Item(6686, 10), new Item(3025, 10), new Item(2435, 15), new Item(9075, 320), new Item(9075, 320), new Item(560, 600), new Item(555, 3000), new Item(565, 450), new Item(557, 5000)),
    PURE("Pure Set", 0, 1, new Item(6107), new Item(6108), new Item(7458), new Item(1712), new Item(4587), new Item(2413), new Item(3842), new Item(6109), new Item(9144, 130), new Item(3105), new Item(10499), new Item(4675), new Item(9185), new Item(5698), new Item(995, 7000000), new Item(386, 220), new Item(2441, 20), new Item(2437, 20), new Item(2443, 20), new Item(2445, 20), new Item(6686, 10), new Item(3025, 10), new Item(2435, 15), new Item(740, 1), new Item(560, 600), new Item(555, 3000), new Item(565, 450), new Item(2497, 1)),
    ZERKER("Zerker Set", 0, 2, new Item(4587), new Item(8850), new Item(3751), new Item(5698), new Item(7462), new Item(1712), new Item(4385), new Item(3105), new Item(1079), new Item(1127), new Item(995, 7000000), new Item(386, 220), new Item(2441, 20), new Item(2437, 20), new Item(2443, 20), new Item(2445, 20), new Item(6686, 10), new Item(3025, 10), new Item(2435, 15), new Item(9075, 320), new Item(560, 600), new Item(555, 3000), new Item(565, 450), new Item(557, 5000)),
    SHARKS("Sharks", 1, -1, new Item(385, 28)),
    SUPER_POT("Super Potions", 1, -1, new Item(2436), new Item(2440), new Item(6685, 2)),
    SUPER_RESTORE("Super Restores", 1, -1, new Item(3024, 3), new Item(6685, 3)),
    RANGING_POTS("Ranging Potions", 1, -1, new Item(2444, 2)),
    BARRAGE_RUNE("Barrage Runes", 1, -1, new Item(560, 5000), new Item(565, 5000), new Item(555, 5000)),
    VENGEANCE_RUNE("Vengeance Runes", 1, -1, new Item(557, 10000), new Item(9075, 5000), new Item(560, 5000)),
    ENTANGE_RUNE("Entangle Runes", 1, -1, new Item(557, 10000), new Item(555, 5000), new Item(561, 5000));

    /**
     * The name.
     */
    private final String name;

    /**
     * The package type.
     */
    private int type;

    /**
     * The spell book.
     */
    private int spellBook;

    /**
     * The items in the package.
     */
    private final Item[] items;

    /**
     * Constructs a new {@Code PKPackage} {@Code Object}
     *
     * @param name  the name.
     * @param items the items.
     */
    PKPackage(String name, int type, int spellBook, Item... items) {
        this.name = name;
        this.type = type;
        this.spellBook = spellBook;
        this.items = items;
    }

    /**
     * Gets the spell book.
     *
     * @return the spell book.
     */
    public int getSpellBook() {
        return spellBook;
    }

    /**
     * Gets the type.
     *
     * @return the type.
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the items.
     *
     * @return the items.
     */
    public Item[] getItems() {
        return items;
    }
}
