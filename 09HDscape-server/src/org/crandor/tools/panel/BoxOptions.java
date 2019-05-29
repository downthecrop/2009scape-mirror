package org.crandor.tools.panel;

/**
 * Package -> ethan
 * Created on -> 8/31/2016 @9:54 PM for CP
 *
 * @author Ethan Kyle Millard
 */
public enum BoxOptions {

    PUNISHMENTS(new String[] {"Temp. Mute", "Perm. Mute", "IP Mute", "Un-mute", "Temp. Ban", "Perm. Ban", "IP Ban", "Un-ban"}),
    TOOLS(new String[] {"NPC Definitions", "Item Definitions "}),
    RANKS(new String[] {"Promote", "Demote"});

    private String[] options;

    BoxOptions(String[] options) {
        this.options = options;
    }

    public String[] getOptions() {
        return options;
    }

}
